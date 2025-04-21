package telran.daily_farm.customer.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import telran.daily_farm.customer.api.dto.*;
import telran.daily_farm.customer.entity.Customer;
import telran.daily_farm.security.api.dto.TokensResponseDto;

public interface ICustomer {
	//Registration and verification
    ResponseEntity<String> registerCustomer(@Valid CustomerRegistrationDto customerDto);//+
    ResponseEntity<String> emailVerification(String verificationToken);//+
    ResponseEntity<String> resendVerificationLink(@Valid String email);//+
    //Authorization and logout
    ResponseEntity<TokensResponseDto> loginCustomer(@Valid LoginRequestDto loginRequestDto);//+
    ResponseEntity<String> logoutCustomer(UUID id, String token);//+
    //Changing and restoring the password
    ResponseEntity<TokensResponseDto> updatePassword(UUID uuid, @Valid ChangePasswordRequest changePasswordDto);//+
    ResponseEntity<String> generateAndSendNewPassword(@Valid String email);//+
    //Updating the customer's data
    ResponseEntity<String> updateCustomer(UUID id, CustomerUpdateDataRequestDto customerDto);//+
    ResponseEntity<String> updatePhone(UUID uuid, @Valid String newPhone);//+
    ResponseEntity<String> changeName(UUID uuid, @Valid FullNameDto fullname);//+
    //Deleting an account
    ResponseEntity<String> removeCustomer(UUID id);//+
    //Email Update
    ResponseEntity<String> sendVerificationTokenForUpdateEmail(UUID id, @Valid String newEmail);//+
    ResponseEntity<String> sendVerificationTokenToNewEmail(String token);
    ResponseEntity<String> updateEmail(@Valid String verificationToken);
	
    Customer getCustomer(UUID customerId);
    
}
