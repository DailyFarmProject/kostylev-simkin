package telran.daily_farm.farmer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.AddressDto;
import telran.daily_farm.api.dto.ChangePasswordRequest;
import telran.daily_farm.api.dto.CoordinatesDto;
import telran.daily_farm.api.dto.FarmerRegistrationDto;
import telran.daily_farm.api.dto.FarmerUpdateDataRequestDto;
import telran.daily_farm.api.dto.FullNameDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.api.dto.TokensResponseDto;
import telran.daily_farm.entity.Address;
import telran.daily_farm.entity.Coordinates;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.entity.FarmerCredential;
import telran.daily_farm.farmer.repo.AddressRepository;
import telran.daily_farm.farmer.repo.CoordinatesRepository;
import telran.daily_farm.farmer.repo.FarmerCredentialRepository;
import telran.daily_farm.farmer.repo.FarmerRepository;
import telran.daily_farm.location.service.ILocationService;
import telran.daily_farm.security.AuthService;
import telran.daily_farm.security.TokenBlacklistService;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FarmerService implements IFarmer {

	private final FarmerRepository farmerRepo;
	private final FarmerCredentialRepository credentialRepo;
	private final CoordinatesRepository coordinatesRepo;
	private final AddressRepository addressRepo;
	private final TokenBlacklistService blackListService;
	private final PasswordEncoder passwordEncoder;
	private final AuthService authService;
	

	@Autowired
	ILocationService locationService;

	@Override
	@Transactional
	public ResponseEntity<String> registerFarmer(FarmerRegistrationDto farmerDto) {
		log.info("Servise. Registration of new farmer - " + farmerDto.getEmail());

		checkEmailIsUnique(farmerDto.getEmail());
		log.info("Servise. Checked. Email is unique");

		Farmer farmer = Farmer.of(farmerDto);
		log.debug("Servise. Created Entity farmer from dto");

		farmer.setBalance(0.);
		log.info("Servise. Starting balanse - 0 added successfully");

		farmerRepo.save(farmer);
		log.info("Servise. Farmer saved to database");
		farmerRepo.flush();

		FarmerCredential credential = FarmerCredential.builder().createdAt(LocalDateTime.now())
				.password_last_updated(LocalDateTime.now())
				.hashedPassword(passwordEncoder.encode(farmerDto.getPassword())).farmer(farmer).build();
		credentialRepo.save(credential);

		Coordinates coordinates = Coordinates.of(
				farmerDto.getCoordinates() == null ? locationService.getCoordinatesFromAddress(farmerDto.getAddress())
						: farmerDto.getCoordinates());
		coordinates.setFarmer(farmer);
		coordinatesRepo.save(coordinates);

		log.info("Servise. Coordinates added successfully ");

		Address address = Address.of(
				farmerDto.getAddress() == null ? locationService.getAddtessFromCoordinates(farmerDto.getCoordinates())
						: farmerDto.getAddress());
		address.setFarmer(farmer);
		addressRepo.save(address);
		log.info("Servise. Address added successfully");

		return ResponseEntity.ok("Farmer added successfully");
	}

	@Override
	@Transactional
	public ResponseEntity<TokensResponseDto> updateFarmer(UUID id, FarmerUpdateDataRequestDto farmerDto) {
		log.info("update farmer data starts");
		
		Farmer farmer = farmerRepo.findByid(id).get();

		String newEmail =  farmerDto.getEmail();
		
		TokensResponseDto tokens = updateEmailIfChanged(farmer,newEmail)?
				authService.authenticate(newEmail, credentialRepo.findByFarmer(farmer).getHashedPassword()): new TokensResponseDto();

		farmer.setFirstName(farmerDto.getFirstName());
		farmer.setLastName(farmerDto.getLastName());
		farmer.setPhone(farmerDto.getPhone());
		farmer.setPaypalDetails(farmerDto.getPaypalDetails());
		

		coordinatesRepo.findByFarmer(new Farmer(id))
				.updateFromDto(farmerDto.getCoordinates() == null
						? locationService.getCoordinatesFromAddress(farmerDto.getAddress())
						: farmerDto.getCoordinates());
		log.info("coordinates updated successfully");

		addressRepo.findByFarmer(new Farmer(id))
				.updateFromDto(farmerDto.getAddress() == null
						? locationService.getAddtessFromCoordinates(farmerDto.getCoordinates())
						: farmerDto.getAddress());
		log.info("address updated successfully");

		return ResponseEntity.ok(tokens);
	}


	@Override
	public ResponseEntity<TokensResponseDto> loginFarmer(LoginRequestDto loginRequestDto) {
		log.debug("Service. Request to login farmer -" + loginRequestDto.getEmail());

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
		if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), oldPassword)) {
			 throw new IllegalArgumentException("Invalid old password");
		}
		credential.setHashedPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
		credential.setPassword_last_updated(LocalDateTime.now());

		TokensResponseDto tokens = 	authService.authenticate(farmer.getEmail(), credential.getHashedPassword());
		
		return ResponseEntity.ok(tokens);
	}
	

	@Override
	@Transactional
	public ResponseEntity<String> updateAddress(UUID id, AddressDto addressDto) {

		addressRepo.findByFarmer(new Farmer(id)).updateFromDto(addressDto);
		coordinatesRepo.findByFarmer(new Farmer(id))
				.updateFromDto(locationService.getCoordinatesFromAddress(addressDto));

		return ResponseEntity.ok("Coordinates and address updated successfully");
	}

	@Override
	@Transactional
	public ResponseEntity<String> updateCoordinates(UUID id, CoordinatesDto coordinatesDto) {

		coordinatesRepo.findByFarmer(new Farmer(id)).updateFromDto(coordinatesDto);

		addressRepo.findByFarmer(new Farmer(id))
				.updateFromDto(locationService.getAddtessFromCoordinates(coordinatesDto));

		return ResponseEntity.ok("Coordinates and address updated successfully");
	}

	@Override
	@Transactional
	
	public ResponseEntity<TokensResponseDto> updateEmail(UUID id, String newEmail) { // todo validation for email
		Farmer farmer = farmerRepo.findByid(id).get();
		farmer.setEmail(newEmail);

		TokensResponseDto tokens = updateEmailIfChanged(farmer,newEmail)?
				authService.authenticate(newEmail, credentialRepo.findByFarmer(farmer).getHashedPassword()): new TokensResponseDto();
		return ResponseEntity.ok(tokens);
	}

	@Override
	@Transactional
	public ResponseEntity<String> updatePhone(UUID id, String newPhone) {
		farmerRepo.findByid(id).get().setPhone(newPhone);
		return ResponseEntity.ok("Phone updated successfully");

	}

	@Override
	@Transactional
	public ResponseEntity<String> changeName(UUID id, FullNameDto fullname) {
		Farmer farmer = farmerRepo.findByid(id).get();
		farmer.setFirstName(fullname.getFirstName());
		farmer.setLastName(fullname.getLastName());
		return ResponseEntity.ok("First name and last name updated successfully");
	}

	
	private void checkEmailIsUnique(String email) {
		if (farmerRepo.existsByEmail(email))
			throw new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_EXISTS);
	}
	
	private boolean updateEmailIfChanged(Farmer farmer, String email) {
		if (!farmer.getEmail().equals(email)) {
			checkEmailIsUnique(email);
			log.info("checked new email is unique + update - successfully");
			farmer.setEmail(email);
			log.info("email is updated - successfully");
			log.info("tokens needs update");
			return true;
		}
		return false;
	}

	@Override
	@Transactional
	public ResponseEntity<String> logoutFarmer(UUID id, String token) {
		log.info("Service. Logout starts");
		FarmerCredential credential = credentialRepo.findByFarmer(new Farmer(id));
		log.info("Service. Got credential - refresh token - " + credential.getRefreshToken());
		log.info("Service. Got access token - " + token);
		token = token.substring(7);
		blackListService.addToBlacklist(token);
		log.info("Service. AccessToken added to black list - " + token);
		credential.setRefreshToken("");
		log.info("Service. RefreshToken remuved from credential");
		return ResponseEntity.ok("Farmer removed");
	}
}
