package telran.daily_farm.customer.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import telran.daily_farm.api.dto.ChangePasswordRequest;
import telran.daily_farm.api.dto.CustomerRegistrationDto;
import telran.daily_farm.api.dto.CustomerUpdateDataRequestDto;
import telran.daily_farm.api.dto.FullNameDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.api.dto.TokensResponseDto;

public interface ICustomer {
    ResponseEntity<String> registerCustomer(@Valid CustomerRegistrationDto customerDto);
    ResponseEntity<String> removeCustomer(UUID id);
    ResponseEntity<TokensResponseDto> loginCustomer(@Valid LoginRequestDto loginRequestDto);
    ResponseEntity<TokensResponseDto> updatePassword(UUID uuid, @Valid ChangePasswordRequest changePasswordDto);
    ResponseEntity<TokensResponseDto> updateEmail(UUID uuid, @Valid String newEmail);
    ResponseEntity<String> updatePhone(UUID uuid, @Valid String newPhone);
    ResponseEntity<String> changeName(UUID uuid, @Valid FullNameDto fullname);
    ResponseEntity<TokensResponseDto> updateCustomer(UUID id, CustomerUpdateDataRequestDto customerDto);
}
