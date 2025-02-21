package telran.daily_farm.client.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import telran.daily_farm.api.dto.ChangePasswordRequest;
import telran.daily_farm.api.dto.ClientRegistrationDto;
import telran.daily_farm.api.dto.ClientUpdateDataRequestDto;
import telran.daily_farm.api.dto.FullNameDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.api.dto.TokensResponseDto;

public interface IClient {
    ResponseEntity<String> registerClient(@Valid ClientRegistrationDto clientDto);
    ResponseEntity<String> removeClient(UUID id);
    ResponseEntity<TokensResponseDto> loginClient(@Valid LoginRequestDto loginRequestDto);
    ResponseEntity<TokensResponseDto> updatePassword(UUID uuid, @Valid ChangePasswordRequest changePasswordDto);
    ResponseEntity<TokensResponseDto> updateEmail(UUID uuid, @Valid String newEmail);
    ResponseEntity<String> updatePhone(UUID uuid, @Valid String newPhone);
    ResponseEntity<String> changeName(UUID uuid, @Valid FullNameDto fullname);
    ResponseEntity<TokensResponseDto> updateClient(UUID id, ClientUpdateDataRequestDto clientDto);
}
