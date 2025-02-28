package telran.daily_farm.security;

import static telran.daily_farm.api.messages.ErrorMessages.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.RefreshTokenResponseDto;
import telran.daily_farm.api.dto.TokensResponseDto;
import telran.daily_farm.customer.repo.CustomerRepository;
import telran.daily_farm.customer.repo.CustomerCredentialRepository;
import telran.daily_farm.entity.Customer;
import telran.daily_farm.entity.CustomerCredential;
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
    private final FarmerCredentialRepository farmerCredentialRepo;
    private final CustomerCredentialRepository customerCredentialRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public TokensResponseDto authenticate(String email, String password) {
        Optional<Customer> customerOptional = customerRepo.findByEmail(email);
        Optional<Farmer> farmerOptional = farmerRepo.findByEmail(email);

        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            CustomerCredential credential = customer.getCredential();

            if (credential != null && passwordEncoder.matches(password, credential.getHashedPassword())) {
                log.info("AuthService. Customer authenticated: {}", email);

                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

                String uuid = customer.getId().toString();
                String accessToken = jwtService.generateAccessToken(uuid, email);
                String refreshToken = jwtService.generateRefreshToken(uuid, email);

                credential.setRefreshToken(refreshToken);
                customerCredentialRepo.save(credential);

                return new TokensResponseDto(accessToken, refreshToken);
            }
        } 

        if (farmerOptional.isPresent()) {
            Farmer farmer = farmerOptional.get();
            FarmerCredential credential = farmerCredentialRepo.findByFarmer(farmer);

            if (credential != null && passwordEncoder.matches(password, credential.getHashedPassword())) {
                log.debug("Authenticate. Farmer {} authenticated", farmer.getEmail());

                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

                String uuid = farmer.getId().toString();
                String accessToken = jwtService.generateAccessToken(uuid, email);
                String refreshToken = jwtService.generateRefreshToken(uuid, email);

                credential.setRefreshToken(refreshToken);
                farmerCredentialRepo.save(credential);

                return new TokensResponseDto(accessToken, refreshToken);
            }
        }

        throw new BadCredentialsException(WRONG_USER_NAME_OR_PASSWORD);
    }

    public ResponseEntity<RefreshTokenResponseDto> refreshAccessToken(String refreshToken) {
        log.info("AuthService. Refresh access token starts - {}", refreshToken);

        UUID id = UUID.fromString(jwtService.extractUserId(refreshToken));

        Optional<Farmer> farmerOptional = farmerRepo.findByid(id);
        Optional<Customer> customerOptional = customerRepo.findById(id);

        if (farmerOptional.isPresent()) {
            Farmer farmer = farmerOptional.get();
            FarmerCredential credential = farmerCredentialRepo.findByFarmer(farmer);

            if (credential != null && refreshToken.equals(credential.getRefreshToken()) && !jwtService.isTokenExpired(refreshToken)) {
                log.info("AuthService. Farmer refresh token is valid");
                return ResponseEntity.ok(new RefreshTokenResponseDto(jwtService.generateAccessToken(id.toString(), farmer.getEmail())));
            }
        } 
        
        if (customerOptional.isPresent()) {
            Customer customer = customerOptional.get();
            CustomerCredential credential = customerCredentialRepo.findByCustomer(customer);

            if (credential != null && refreshToken.equals(credential.getRefreshToken()) && !jwtService.isTokenExpired(refreshToken)) {
                log.info("AuthService. Customer refresh token is valid");
                return ResponseEntity.ok(new RefreshTokenResponseDto(jwtService.generateAccessToken(id.toString(), customer.getEmail())));
            }
        }

        throw new RuntimeException("Invalid or expired refresh token");
    }
}
