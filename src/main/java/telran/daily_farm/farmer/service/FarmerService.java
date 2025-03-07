package telran.daily_farm.farmer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.*;
import telran.daily_farm.email_sender.service.MailSenderService;
import telran.daily_farm.email_sender.service.SendGridEmailSender;
import telran.daily_farm.entity.*;
import telran.daily_farm.farmer.repo.CoordinatesRepository;
import telran.daily_farm.farmer.repo.FarmerCredentialRepository;
import telran.daily_farm.farmer.repo.FarmerRepository;
import telran.daily_farm.location.service.ILocationService;
import telran.daily_farm.security.AuthService;
import telran.daily_farm.security.JwtService;
import telran.daily_farm.security.TokenBlacklistService;
import telran.daily_farm.translate_service.LibreTranslateLocalService;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FarmerService implements IFarmer {

	private final FarmerRepository farmerRepo;
	private final FarmerCredentialRepository credentialRepo;
	private final CoordinatesRepository coordinatesRepo;
	private final LibreTranslateLocalService libreTranslate;
	//private final AddressRepository addressRepo;
	private final TokenBlacklistService blackListService;

	private final StringRedisTemplate redisTemplate;
	private final PasswordEncoder passwordEncoder;
	private final AuthService authService;
	private final MailSenderService emailService;
	private final SendGridEmailSender gridSender;
	private final JwtService jwtService;

	@Autowired
	ILocationService locationService;

	@Override
	@Transactional
	public ResponseEntity<String> registerFarmer(FarmerRegistrationDto farmerDto , String lang) {
		log.info("Servise. Registration of new farmer - " + farmerDto.getEmail());
		String email = farmerDto.getEmail();
		checkEmailIsUnique(email);
		log.info("Servise. Checked. Email is unique");

		Farmer farmer = Farmer.of(farmerDto);
		log.debug("Servise. Created Entity farmer from dto");

		farmer.setBalance(0.);
		log.info("Servise. Starting balanse - 0 added successfully");

		farmerRepo.save(farmer);
		log.info("Servise. Farmer saved to database");
		farmerRepo.flush();
		
		redisTemplate.opsForValue().set("user:language:" + farmer.getId(), lang);

		FarmerCredential credential = FarmerCredential.builder().createdAt(LocalDateTime.now())
				.password_last_updated(LocalDateTime.now())
				.hashedPassword(passwordEncoder.encode(farmerDto.getPassword())).farmer(farmer).build();
		credentialRepo.save(credential);
		Coordinates coordinates = Coordinates.of(farmerDto.getCoordinates());
//				farmerDto.getCoordinates() == null ? locationService.getCoordinatesFromAddress(farmerDto.getAddress())
//						: );
		coordinates.setFarmer(farmer);
		coordinatesRepo.save(coordinates);

		log.info("Servise. Coordinates added successfully ");

//		Address address = Address.of(
//				farmerDto.getAddress() == null ? locationService.getAddtessFromCoordinates(farmerDto.getCoordinates())
//						: farmerDto.getAddress());
//		address.setFarmer(farmer);
//		addressRepo.save(address);
		log.info("Service. Address added successfully");

		gridSender.sendEmailVerification(email,
				jwtService.generateVerificationToken(farmer.getId().toString(), email));

		return ResponseEntity.ok("Farmer added successfully. You need to verify your email");
	}

	@Override
	@Transactional
	public ResponseEntity<String> updateFarmer(UUID id, FarmerUpdateDataRequestDto farmerDto) {
		log.info("Service. Update farmer data starts");

		Farmer farmer = farmerRepo.findByid(id).get();
		farmer.setPhone(farmerDto.getPhone());
		log.info("Service. Phone updated");
		farmer.setCompany(farmerDto.getCompany());
		log.info("Service. Phone updated");
		coordinatesRepo.findByFarmer(new Farmer(id)).updateFromDto(farmerDto.getCoordinates());
		log.info("coordinates updated successfully");

		return ResponseEntity.ok("Data updated successfuly");
	}

	@Override
	public ResponseEntity<TokensResponseDto> loginFarmer(LoginRequestDto loginRequestDto) {
		String email = loginRequestDto.getEmail();
		log.debug("Service. Request to login farmer -" + email);

		Farmer farmer = farmerRepo.findByEmail(email).orElseThrow(()->
			new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS));
		
		if(!credentialRepo.findByFarmer(farmer).isVerificated()) {
			log.debug("Service. Login. Email is not verificated. Send link to email -" + email);
			gridSender.sendEmailVerification(email,
					jwtService.generateVerificationToken(farmer.getId().toString(), email));
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, EMAIL_IS_NOT_VERIFICATED);
		}
		
		TokensResponseDto tokens = authService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());
		log.debug("Service. Login successfull, token returned to user");
		return ResponseEntity.ok(tokens);
	}

	@Override
	@Transactional
	public ResponseEntity<String> removeFarmer(UUID id) {
		if (!farmerRepo.existsById(id))
			throw new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS);
		log.info("user exists");
		farmerRepo.deleteById(id);
		return ResponseEntity.ok("Farmer removed");
	}

	@Override
	@Transactional
	public ResponseEntity<TokensResponseDto> updatePassword(UUID id, ChangePasswordRequest changePasswordDto) {
		Farmer farmer = farmerRepo.findByid(id).get();
		FarmerCredential credential = credentialRepo.findByFarmer(farmer);
		String oldPassword = credential.getHashedPassword();
		log.info("Service. updatePassword. Old password is - " + oldPassword);
		if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), oldPassword)) {
			throw new IllegalArgumentException("Invalid old password");
		}
		log.info("Service. updatePassword. Old password - " + credential.getHashedPassword());
		credential.setHashedPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		log.info("Service. updatePassword. New password - " + credential.getHashedPassword());
		credential.setPassword_last_updated(LocalDateTime.now());

		TokensResponseDto tokens = authService.authenticate(farmer.getEmail(), changePasswordDto.getNewPassword() );

		return ResponseEntity.ok(tokens);
	}

//	@Override
//	@Transactional
//	public ResponseEntity<String> updateAddress(UUID id, AddressDto addressDto) {
//
//		addressRepo.findByFarmer(new Farmer(id)).updateFromDto(addressDto);
//		coordinatesRepo.findByFarmer(new Farmer(id))
//				.updateFromDto(locationService.getCoordinatesFromAddress(addressDto));
//
//		return ResponseEntity.ok("Coordinates and address updated successfully");
//	}

	@Override
	@Transactional
	public ResponseEntity<String> updateCoordinates(UUID id, CoordinatesDto coordinatesDto) {
		coordinatesRepo.findByFarmer(new Farmer(id)).updateFromDto(coordinatesDto);
//		addressRepo.findByFarmer(new Farmer(id))
//				.updateFromDto(locationService.getAddtessFromCoordinates(coordinatesDto));
		return ResponseEntity.ok("Coordinates and address updated successfully");
	}
	
	@Override

	public ResponseEntity<String> sendVerificationTokenForUpdateEmail(UUID id, String newEmail) { // todo validation for email
		Optional<Farmer> optFarmer = farmerRepo.findByEmail(newEmail);
		if(optFarmer.isPresent())
			throw new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_EXISTS);
		
		Farmer farmer = farmerRepo.findByid(id).get();
		String email = farmer.getEmail();
		gridSender.sendChangeEmailVerification(email, jwtService.generateVerificationTokenForUpdateEmail(farmer.getId().toString(),email, newEmail));
		return ResponseEntity.ok(CHECK_EMAIL_FOR_VERIFICATION_LINK + " - " + email);
	}
	
	@Override
	public ResponseEntity<String> sendVerificationTokenToNewEmail(String verificationToken) {
		
		String newEmailFromToken = jwtService.extractUserNewEmail(verificationToken);
		String oldEmailFromToken = jwtService.extractUserEmail(verificationToken);
		
		if (jwtService.isTokenValid(verificationToken, oldEmailFromToken)
				 && !blackListService.isBlacklisted(verificationToken)) {
			String id = jwtService.extractUserId(verificationToken);
			String newToken = jwtService.generateVerificationTokenForUpdateEmail(id, oldEmailFromToken, newEmailFromToken);
			gridSender.sendVerificationTokenToNewEmail(newEmailFromToken, newToken);
			blackListService.addToBlacklist(verificationToken);
		}else
			throw new JwtException(INVALID_TOKEN);
		return ResponseEntity.ok(CHECK_EMAIL_FOR_VERIFICATION_LINK + " - " + newEmailFromToken);
	}
	
	@Override
	@Transactional
	public ResponseEntity<String> updateEmail(String verificationToken) { // todo validation for email
		
		
		log.info("Service. Request updateEmail");
		try {
			String oldEmailFromToken = jwtService.extractUserEmail(verificationToken);
			String newEmailFromToken = jwtService.extractUserNewEmail(verificationToken);
			log.info("Service.updateEmail Email from token - " + oldEmailFromToken);
			log.info("Service.updateEmail New email from token - " + newEmailFromToken);
			if (jwtService.isTokenValid(verificationToken, oldEmailFromToken)
					 && !blackListService.isBlacklisted(verificationToken)) {
				Farmer farmer = farmerRepo.findByEmail(oldEmailFromToken).orElseThrow(
						() -> new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS));
				Optional<Farmer> optFarmer = farmerRepo.findByEmail(newEmailFromToken);
				if(optFarmer.isPresent())
					throw new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_EXISTS);
				log.info("Service.updateEmail Farmer exists");
				farmer.setEmail(newEmailFromToken);
				log.info("Service.updateEmail New email saved - " + newEmailFromToken);
				blackListService.addToBlacklist(verificationToken);
				log.info("Service.updateEmail. Token added to blacklist");
			}else
				throw new JwtException(INVALID_TOKEN);
		} catch (Exception e) {
			log.error("Service. invalid token" + INVALID_TOKEN);
			throw new JwtException(INVALID_TOKEN);
		}
		return ResponseEntity.ok(EMAIL_IS_VERIFICATED);
	}


	@Override
	@Transactional
	public ResponseEntity<String> updatePhone(UUID id, String newPhone) {
		farmerRepo.findByid(id).get().setPhone(newPhone);
		return ResponseEntity.ok(libreTranslate.translate("","","Phone updated successfully"));

	}

	private void checkEmailIsUnique(String email) {
		if (farmerRepo.existsByEmail(email))
			throw new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_EXISTS);
	}

	@Override
	@Transactional
	public ResponseEntity<String> logoutFarmer(UUID id, String token) {
		log.info("Service.logoutFarmer Logout starts");
		FarmerCredential credential = credentialRepo.findByFarmer(new Farmer(id));
		log.info("Service.logoutFarmer Got credential - refresh token");
		log.info("Service.logoutFarmer Got access token");
		token = token.substring(7);
		blackListService.addToBlacklist(token);
		log.info("Service.logoutFarmer AccessToken added to black list");
		credential.setRefreshToken("");
		log.info("Service.logoutFarmer RefreshToken removed from credential");
		return ResponseEntity.ok("Farmer removed");
	}

	@Override
	@Transactional
	public ResponseEntity<String> emailVerification(String verificationToken) {
		log.info("Service.emailVerification. Request to email verification");
		try {
			
			String emailFromToken = jwtService.extractUserEmail(verificationToken);
			log.info("Service.emailVerification. Email from token - " + emailFromToken);
			if (jwtService.isTokenValid(verificationToken, emailFromToken)
					&& !jwtService.isTokenExpired(verificationToken) && !blackListService.isBlacklisted(verificationToken)) {
				log.info("Service.emailVerification. Token is valid - " + emailFromToken);
				Farmer farmer = farmerRepo.findByEmail(emailFromToken).orElseThrow(
						() -> new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS));
				log.info("Service.emailVerification Farmer exists");
				FarmerCredential credential = credentialRepo.findByFarmer(farmer);

				credential.setVerificated(true);
				blackListService.addToBlacklist(verificationToken);
				log.info("Service.emailVerification Set  verificated true - " + emailFromToken);
			}else
				throw new JwtException(INVALID_TOKEN);
		} catch (Exception e) {
			log.error("Service.emailVerification Invalid token" + INVALID_TOKEN);
			throw new JwtException(INVALID_TOKEN);
		}
		return ResponseEntity.ok(EMAIL_IS_VERIFICATED);
	}

	@Override
	public ResponseEntity<String> resendVerificationLink(String email) {
		Farmer farmer = farmerRepo.findByEmail(email).orElseThrow(()->
			new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS));

		emailService.sendEmailVerification(email, jwtService.generateVerificationToken(farmer.getId().toString(),email));
		return ResponseEntity.ok(CHECK_EMAIL_FOR_VERIFICATION_LINK);
	}

	@Override
	@Transactional
	public ResponseEntity<String> generateAndSendNewPassword(String email) {
		Farmer farmer = farmerRepo.findByEmail(email).orElseThrow(()->
			new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS));
		log.info("Service. getResetPassword(). Farmer exists");
		
		FarmerCredential credential = credentialRepo.findByFarmer(farmer);
		String genPassword = jwtService.generatePassword(10);
		credential.setHashedPassword(passwordEncoder.encode(genPassword));
		
		log.info("Service. Password hashed and saved to credential");
		credential.setPassword_last_updated(LocalDateTime.now());
		
		gridSender.sendResetPassword(email, genPassword);
		log.info("Service. Password was send to email ");
		
	return ResponseEntity.ok(CHECK_EMAIL_FOR_VERIFICATION_LINK);
	}

	@Override
	@Transactional
	public ResponseEntity<String> updateCompany(UUID id, String company) {
		farmerRepo.findByid(id).get().setCompany(company);
		return ResponseEntity.ok("Company updated successfully");
	}
}
