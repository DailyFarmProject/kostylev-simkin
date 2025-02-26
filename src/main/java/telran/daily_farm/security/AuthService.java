package telran.daily_farm.security;

import static telran.daily_farm.api.messages.ErrorMessages.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.RefreshTokenResponseDto;
import telran.daily_farm.api.dto.TokensResponseDto;
import telran.daily_farm.customer.repo.CustomerRepository;
import telran.daily_farm.entity.Customer;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.entity.FarmerCredential;
import telran.daily_farm.farmer.repo.FarmerCredentialRepository;
import telran.daily_farm.farmer.repo.FarmerRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
	private final FarmerRepository farmerRepo;
	private final CustomerRepository customerRepo;
	private final FarmerCredentialRepository credentialRepo;
	private final JwtService jwtService;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	public TokensResponseDto authenticate(String email, String password) {

		Optional<Farmer> farmerOptional = farmerRepo.findByEmail(email);

		Optional<Customer> customerOptional = customerRepo.findByEmail(email);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            if (passwordEncoder.matches(password, customer.getCredential().getHashedPassword())) {
                log.info("AuthService. Customer authenticated: {}", email);
                
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                
                String uuid = customer.getId().toString();
                String accessToken = jwtService.generateAccessToken(uuid, email);
                String refreshToken = jwtService.generateRefreshToken(uuid, email);
                
                return new TokensResponseDto(accessToken, refreshToken);
            }
        } else
		if (farmerOptional.isPresent()) {
			Farmer farmer = farmerOptional.get();
			FarmerCredential credential = credentialRepo.findByFarmer(farmer);
			log.debug("Authenticate. Farmer " + farmer.getEmail() + " exists");
			if (passwordEncoder.matches(password, credential.getHashedPassword())) {
				log.debug("Authenticate. Password is valid");
				String uuid = farmer.getId().toString();
				String accessToken = jwtService.generateAccessToken(uuid, email);
				String refreshToken = jwtService.generateRefreshToken(uuid, email);
				credential.setRefreshToken(refreshToken);
				credentialRepo.save(credential);
				authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

				return new TokensResponseDto(accessToken, refreshToken);
			}
		}
		throw new BadCredentialsException(WRONG_USER_NAME_OR_PASSWORD);
	}

	public ResponseEntity<RefreshTokenResponseDto> refreshAccessToken(String refreshToken) {
		
		log.info("AuthService. Refresh access token starts - " + refreshToken );
		
		UUID id = UUID.fromString(jwtService.extractUserId(refreshToken));
		Optional<Farmer> farmerOptional = farmerRepo.findByid(id);
		FarmerCredential credential = credentialRepo.findByFarmer(new Farmer(id));

		if (farmerOptional.isPresent() && credential.getRefreshToken().equals(refreshToken) && !jwtService.isTokenExpired(refreshToken)) {
			log.info("AuthService. Checking data from refreshToken : farmer exists - " + farmerOptional.isPresent());
			log.info("AuthService. Checking data from refreshToken : credential.getRefreshToken().equals(refreshToken) exists - " + credential.getRefreshToken().equals(refreshToken));
			log.info("AuthService. Checking data from refreshToken : isTokenExpired - " + jwtService.isTokenExpired(refreshToken));
			return ResponseEntity.ok(new RefreshTokenResponseDto(jwtService.generateAccessToken(id.toString(), farmerOptional.get().getEmail())));
		} else {
			throw new RuntimeException("Invalid or expired refresh token");
		}
	}
}