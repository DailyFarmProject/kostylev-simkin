package telran.daily_farm.farmer.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import jakarta.validation.Valid;
import telran.daily_farm.api.dto.AddressDto;
import telran.daily_farm.api.dto.ChangePasswordRequest;
import telran.daily_farm.api.dto.CoordinatesDto;
import telran.daily_farm.api.dto.FarmerRegistrationDto;
import telran.daily_farm.api.dto.FarmerUpdateDataRequestDto;
import telran.daily_farm.api.dto.FullNameDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.api.dto.TokensResponseDto;



public interface IFarmer {
	
	ResponseEntity<String> registerFarmer(FarmerRegistrationDto farmerDto);
	ResponseEntity<String> emailVerification(String verificationToken);
	ResponseEntity<String> getVerificationLink(String email);
	ResponseEntity<String> removeFarmer(UUID id);
	ResponseEntity<TokensResponseDto> loginFarmer(LoginRequestDto loginRequestDto);
	ResponseEntity<String> logoutFarmer(UUID id, String token);
	ResponseEntity<TokensResponseDto> updatePassword(UUID uuid, ChangePasswordRequest changePasswordDto);
	ResponseEntity<String> updateAddress(UUID uuid,  AddressDto addressDto);
	ResponseEntity<String> updateCoordinates(UUID uuid, CoordinatesDto coordinatesDto);
	ResponseEntity<TokensResponseDto> updateEmail(UUID uuid, String newEmail);
	ResponseEntity<String> updatePhone(UUID uuid, String newPhone);
	ResponseEntity<String> changeName(UUID uuid, FullNameDto fullname);
	ResponseEntity<TokensResponseDto> updateFarmer(UUID id, FarmerUpdateDataRequestDto farmerDto);

}
