package telran.daily_farm.security;

import static telran.daily_farm.api.messages.ErrorMessages.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.RefreshTokenResponseDto;
import telran.daily_farm.api.dto.TokensResponseDto;
import telran.daily_farm.customer.repo.CustomerCredentialRepository;
import telran.daily_farm.customer.repo.CustomerRepository;
import telran.daily_farm.entity.Customer;
import telran.daily_farm.entity.CustomerCredential;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.entity.FarmerCredential;
import telran.daily_farm.farmer.repo.FarmerCredentialRepository;
import telran.daily_farm.farmer.repo.FarmerRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final FarmerRepository farmerRepo;
	private final FarmerCredentialRepository credentialRepo;
	private final CustomerRepository customerRepo;
	private final CustomerCredentialRepository customerCredentialRepo;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;

	public TokensResponseDto authenticateFarmer(String email, String password) {

		Optional<Farmer> farmerOptional = farmerRepo.findByEmail(email);

		if (farmerOptional.isPresent()) {
			Farmer farmer = farmerOptional.get();
			FarmerCredential credential = credentialRepo.findByFarmer(farmer);
			log.info("Authenticate. Farmer " + farmer.getEmail() + " exists");

			log.info("Authenticate. passwordEncoder.matches"
					+ passwordEncoder.matches(password, credential.getHashedPassword()));
			if (passwordEncoder.matches(password, credential.getHashedPassword())) {
				log.info("Authenticate. Password is valid");
				String uuid = farmer.getId().toString();

				String accessToken = jwtService.generateAccessToken(uuid, email, "farmer");
				log.info("access token - " + accessToken);
				String refreshToken = jwtService.generateRefreshToken(uuid, email, "farmer");
				log.info("refresh token - " + refreshToken);

				credential.setRefreshToken(refreshToken);
				credentialRepo.save(credential);
				
				log.info("login success!!! ");
				return new TokensResponseDto(accessToken, refreshToken);
			}
		}
		throw new BadCredentialsException(WRONG_USER_NAME_OR_PASSWORD);
	}

	public TokensResponseDto authenticateCustomer(String email, String password) {

		Optional<Customer> customerOptional = customerRepo.findByEmail(email);
		if (customerOptional.isPresent()) {
			Customer customer = customerOptional.get();
			CustomerCredential credential = customerCredentialRepo.findByCustomer(customer);
			log.info("Authenticate. Customer " + customer.getEmail() + " exists");
			log.info("Authenticate. passwordEncoder.matches"
					+ passwordEncoder.matches(password, credential.getHashedPassword()));
			if (passwordEncoder.matches(password, credential.getHashedPassword())) {
				log.info("Authenticate. Password is valid");
				String uuid = customer.getId().toString();
				String accessToken = jwtService.generateAccessToken(uuid, email, "customer");
				String refreshToken = jwtService.generateRefreshToken(uuid, email, "customer");
				credential.setRefreshToken(refreshToken);
				customerCredentialRepo.save(credential);
				return new TokensResponseDto(accessToken, refreshToken);
			}
		}
		throw new BadCredentialsException(WRONG_USER_NAME_OR_PASSWORD);
	}

	
	//TODO refreshAccessTokenFarmer + refreshAccessTokenCustomer
	public ResponseEntity<RefreshTokenResponseDto> refreshAccessTokenFarmer(String refreshToken) {

		log.info("AuthService refreshAccessToken. Refresh access token starts - " + refreshToken);

		UUID id = UUID.fromString(jwtService.extractUserId(refreshToken));
		Optional<Farmer> farmerOptional = farmerRepo.findByid(id);
		FarmerCredential credential = credentialRepo.findByFarmer(new Farmer(id));
		log.info("AuthService refreshAccessToken. Checking data from refreshToken : farmer exists - "
				+ farmerOptional.isPresent());
		log.info("AuthService refreshAccessToken. credential.getRefreshToken().equals(refreshToken) - "
				+ credential.getRefreshToken().equals(refreshToken));
		log.info("AuthService refreshAccessToken. isTokenExpired - " + jwtService.isTokenExpired(refreshToken));
		if (farmerOptional.isPresent() && !credential.getRefreshToken().isBlank()
				&& credential.getRefreshToken().equals(refreshToken) && !jwtService.isTokenExpired(refreshToken)) {
			return ResponseEntity.ok(new RefreshTokenResponseDto(
					jwtService.generateAccessToken(id.toString(), farmerOptional.get().getEmail(), jwtService.extractUserRole(refreshToken))));
		} 
		throw new BadCredentialsException(INVALID_TOKEN);
	}
	
	public ResponseEntity<RefreshTokenResponseDto> refreshAccessTokenCustomer(String refreshToken) {

		log.info("AuthService refreshAccessToken. Refresh access token starts - " + refreshToken);

		UUID id = UUID.fromString(jwtService.extractUserId(refreshToken));
		Optional<Customer> customerOptional = customerRepo.findById(id);
		CustomerCredential customerCredential = customerCredentialRepo.findByCustomer(new Customer(id));
		if (customerOptional.isPresent() && !customerCredential.getRefreshToken().isBlank()
				&& customerCredential.getRefreshToken().equals(refreshToken)
				&& !jwtService.isTokenExpired(refreshToken)) {
			return ResponseEntity.ok(new RefreshTokenResponseDto(
					jwtService.generateAccessToken(id.toString(), customerOptional.get().getEmail(),jwtService.extractUserRole(refreshToken))));
		}
		throw new BadCredentialsException(INVALID_TOKEN);
	}
	
	
}
