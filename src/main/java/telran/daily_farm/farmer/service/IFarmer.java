package telran.daily_farm.farmer.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import telran.daily_farm.api.dto.ChangePasswordRequest;
import telran.daily_farm.api.dto.CoordinatesDto;
import telran.daily_farm.api.dto.FarmerRegistrationDto;
import telran.daily_farm.api.dto.FarmerUpdateDataRequestDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.api.dto.TokensResponseDto;

public interface IFarmer {

	ResponseEntity<String> registerFarmer(FarmerRegistrationDto farmerDto, String lang);

	ResponseEntity<String> emailVerification(String verificationToken);

	ResponseEntity<String> resendVerificationLink(String email);

	ResponseEntity<String> removeFarmer(UUID id);

	ResponseEntity<TokensResponseDto> loginFarmer(LoginRequestDto loginRequestDto);

	ResponseEntity<String> logoutFarmer(UUID id, String token);

	ResponseEntity<TokensResponseDto> updatePassword(UUID uuid, ChangePasswordRequest changePasswordDto);
	
	ResponseEntity<String> generateAndSendNewPassword(String email);

	ResponseEntity<String> updateCoordinates(UUID uuid, CoordinatesDto coordinatesDto);

	ResponseEntity<String> updatePhone(UUID uuid, String newPhone);

	ResponseEntity<String> updateFarmer(UUID id, FarmerUpdateDataRequestDto farmerDto);

	ResponseEntity<String> sendVerificationTokenForUpdateEmail(UUID id, String newEmail);

	ResponseEntity<String> sendVerificationTokenToNewEmail(String token);
	
	ResponseEntity<String> updateEmail(String token);

	ResponseEntity<String> updateCompany(UUID id, String company);

	// ResponseEntity<String> updateAddress(UUID uuid, AddressDto addressDto);
	// ResponseEntity<String> changeName(UUID uuid, FullNameDto fullname);
}
