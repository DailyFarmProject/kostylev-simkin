package telran.daily_farm.customer.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.*;
import telran.daily_farm.customer.repo.CustomerCredentialRepository;
import telran.daily_farm.customer.repo.CustomerRepository;
import telran.daily_farm.entity.Customer;
import telran.daily_farm.entity.CustomerCredential;
import telran.daily_farm.security.AuthService;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerService implements ICustomer {

    private final CustomerRepository customerRepo;
    private final CustomerCredentialRepository credentialRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Override
    @Transactional
    public ResponseEntity<String> registerCustomer(CustomerRegistrationDto customerDto) {
        log.info("Service: Registering customer - " + customerDto.getEmail());

        checkEmailIsUnique(customerDto.getEmail());
        Customer customer = Customer.of(customerDto);
        customerRepo.save(customer);

        CustomerCredential credential = CustomerCredential.builder()
                .createdAt(LocalDateTime.now())
                .passwordLastUpdated(LocalDateTime.now())
                .hashedPassword(passwordEncoder.encode(customerDto.getPassword()))
                .customer(customer)
                .build();
        credentialRepo.save(credential);

        return ResponseEntity.ok("Customer registered successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<TokensResponseDto> loginCustomer(LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
    }

    @Override
    @Transactional
    public ResponseEntity<String> removeCustomer(UUID id) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        customerRepo.delete(customer);
        return ResponseEntity.ok("Customer removed successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<TokensResponseDto> updatePassword(UUID id, ChangePasswordRequest changePasswordDto) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
        CustomerCredential credential = credentialRepo.findByCustomer(customer);

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), credential.getHashedPassword()))
            throw new IllegalArgumentException("Invalid old password");

        credential.setHashedPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        credential.setPasswordLastUpdated(LocalDateTime.now());

        return ResponseEntity.ok(authService.authenticate(customer.getEmail(), credentialRepo.findByCustomer(customer).getHashedPassword()));
    }

    @Override
    @Transactional
    public ResponseEntity<TokensResponseDto> updateEmail(UUID id, String newEmail) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    
        checkEmailIsUnique(newEmail);
        customer.setEmail(newEmail);

        return ResponseEntity.ok(authService.authenticate(customer.getEmail(), credentialRepo.findByCustomer(customer).getHashedPassword()));
    }

    @Override
    @Transactional
    public ResponseEntity<String> updatePhone(UUID id, String newPhone) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    
        customer.setPhone(newPhone);
        return ResponseEntity.ok("Phone updated successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<String> changeName(UUID id, FullNameDto fullname) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));
    
        customer.setFirstName(fullname.getFirstName());
        customer.setLastName(fullname.getLastName());
        return ResponseEntity.ok("First name and last name updated successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<TokensResponseDto> updateCustomer(UUID id, CustomerUpdateDataRequestDto customerDto) {
        Customer customer = customerRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Customer not found"));

        String newEmail = customerDto.getEmail();
        TokensResponseDto tokens = customer.getEmail().equals(newEmail) ? 
                new TokensResponseDto() : authService.authenticate(newEmail, credentialRepo.findByCustomer(customer).getHashedPassword());

        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPhone(customerDto.getPhone());

        return ResponseEntity.ok(tokens);
    }

    private void checkEmailIsUnique(String email) {
        if (customerRepo.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Customer with this email already exists");
    }
}
