package telran.daily_farm.farmer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.AddressDto;
import telran.daily_farm.api.dto.ChangePasswordRequest;
import telran.daily_farm.api.dto.CoordinatesDto;
import telran.daily_farm.api.dto.FarmerRegistrationDto;
import telran.daily_farm.api.dto.FarmerUpdateDataRequestDto;
import telran.daily_farm.api.dto.FullNameDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.api.dto.RefreshTokenRequestDto;
import telran.daily_farm.api.dto.RefreshTokenResponseDto;
import telran.daily_farm.api.dto.TokensResponseDto;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.entity.FarmerCredential;
import telran.daily_farm.farmer.service.IFarmer;
import telran.daily_farm.security.AuthService;
import telran.daily_farm.security.JwtService;
import telran.daily_farm.security.UserDetailsWithId;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import java.util.UUID;

import static telran.daily_farm.api.ApiConstants.*;

@Tag(name = "Farmer API", description = "Methods for farmer")
@RestController
@Slf4j
public class FarmerController {

	@Autowired
	IFarmer farmerService;
	@Autowired
	JwtService jwtService;
	@Autowired
	private AuthService authService;

	@Operation(summary = "Registration of new Farmer", description = "Registration of new Farmer", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Registration data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FarmerRegistrationDto.class))))
	@PostMapping(FARMER_REGISTER)
	public ResponseEntity<String> registerFarmer(@Valid @RequestBody FarmerRegistrationDto farmerDto,
			@RequestHeader(value = "X-Latitude", required = false) Double latitude,
			@RequestHeader(value = "X-Longitude", required = false) Double longitude) {

		log.info("Controller. Request for registration new farmer - " + farmerDto.getEmail());
		if (!hasValidLocation(farmerDto, latitude, longitude))
			return ResponseEntity.badRequest().body(LOCATION_REQUIRED);

		if (latitude != null && longitude != null && farmerDto.getCoordinates() == null) {
			farmerDto.setCoordinates(new CoordinatesDto(latitude, longitude));
			log.debug("Controller. There is not coordinates in body. Update coordinates in dto from header");
		}
		return farmerService.registerFarmer(farmerDto);
	}
	
	@Operation(summary = "Logout of Farmer", description = "Logout of Farmer. Removes refreshToken, Adds accessToken to blacklist", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data for logout", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@DeleteMapping(FARMER_LOGOUT)
	public ResponseEntity<String> logoutFarmer(	@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		farmerService.logoutFarmer(user.getId(), token);
		return ResponseEntity.ok("Logged out successfully");
	}

	@Operation(summary = "Login of Farmer", description = "Login. Return accessToken and refreshToken", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Data for login", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequestDto.class))))
	@PostMapping(FARMER_LOGIN)
	public ResponseEntity<TokensResponseDto> login(@RequestBody LoginRequestDto loginRequestDto
		) {
		return farmerService.loginFarmer(loginRequestDto);
	}

	@Operation(summary = "Refresh token", description = "When accessToken expired any request thows exception. Need to refresh token by sending refreshToken to this endpoint. Return new  accessToken", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "accessToken", content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@GetMapping(FARMER_REFRESH_TOKEN)
	public ResponseEntity<RefreshTokenResponseDto> refresh(
			@Parameter(description = "JWT токен", required = true) @RequestBody RefreshTokenRequestDto request) {
		log.info("Controller/ refresh token starts");
		return authService.refreshAccessToken(request.getRefreshToken());
	}

	@Operation(summary = "Update all farmer's data", description = "Option to update all (all not requared, instead of password) data with sending one form. If email updated, return new tokens", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Update data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = FarmerUpdateDataRequestDto.class))))
	@PutMapping(FARMER_EDIT)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<TokensResponseDto> updateFarmer(@Valid @RequestBody FarmerUpdateDataRequestDto farmerDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updateFarmer(user.getId(), farmerDto);
	}

	@Operation(summary = "Remove Farmer", description = "Remove Farmer from database", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FarmerUpdateDataRequestDto.class))))
	@DeleteMapping(FARMER_REMOVE)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> removeFarmer(@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.removeFarmer(user.getId());
	}

	@Operation(summary = "Update password", description = "Update password. Return new tokens", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChangePasswordRequest.class))))
	@PutMapping(FARMER_CHANGE_PASSWORD)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<TokensResponseDto> farmerUpdatePassword(
			@Valid @RequestBody ChangePasswordRequest changePasswordDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updatePassword(user.getId(), changePasswordDto);
	}

	@Operation(summary = "Update Farmer's address", description = "Update Farmer's address.", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные нового адреса", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDto.class))))
	@PutMapping(FARMER_CHANGE_ADDRESS)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> farmerUpdateAddress(@Valid @RequestBody AddressDto addressDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updateAddress(user.getId(), addressDto);
	}

	@Operation(summary = "Update Farmer's coordinates", description = "Update Farmer's coordinates. Updates the address according to the new coordinates ", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoordinatesDto.class))))
	@PutMapping(FARMER_CHANGE_COORDINATES)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> farmerUpdateCoordinates(@Valid @RequestBody CoordinatesDto coordinatesDto,
			@RequestHeader(value = "X-Latitude", required = false) Double latitude,
			@RequestHeader(value = "X-Longitude", required = false) Double longitude,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {

		if ((latitude == null && longitude == null) && coordinatesDto == null)
			return ResponseEntity.badRequest().body(LOCATION_REQUIRED);
		if (coordinatesDto == null)
			coordinatesDto = new CoordinatesDto(latitude, longitude);

		return farmerService.updateCoordinates(user.getId(), coordinatesDto);
	}

	@Operation(summary = "Update Farmer's email", description = "Update Farmer's email. Return new tokens ", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@PutMapping(FARMER_CHANGE_EMAIL)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<TokensResponseDto> farmerUpdateEmail(@Valid @RequestBody String newEmail,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updateEmail(user.getId(), newEmail);
	}

	@Operation(summary = "Update Farmer's phone number", description = "Update Farmer's phone number", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@PutMapping(FARMER_CHANGE_PHONE)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> farmerUpdatePhone(@Valid @RequestBody String newPhone,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updatePhone(user.getId(), newPhone);
	}

	@Operation(summary = "Update Farmer's first name and last name", description = "Update Farmer's first name and last name", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullNameDto.class))))
	@PreAuthorize("hasRole(ROLE_FARMER)")
	@PutMapping(FARMER_CHANGE_FIRST_LAST_NAME_PASSWORD)
	public ResponseEntity<String> farmerUpdateName(@Valid @RequestBody FullNameDto fullname,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.changeName(user.getId(), fullname);
	}

	private boolean hasValidLocation(@Valid FarmerRegistrationDto farmerDto, Double latitude, Double longitude) {

		boolean hasCoordinatesInBody = farmerDto.getCoordinates() != null
				&& farmerDto.getCoordinates().getLatitude() != null
				&& farmerDto.getCoordinates().getLongitude() != null;
		log.debug("Controller. Checking is request contains coordinate in body. Result - " + hasCoordinatesInBody);

		boolean hasAddressInBody = farmerDto.getAddress() != null && farmerDto.getAddress().getCountry() != null
				&& farmerDto.getAddress().getCity() != null && farmerDto.getAddress().getPostalCode() != null;
		log.debug("Controller. Checking is request contains address in body. Result - " + hasAddressInBody);

		boolean hasCoordinatesInHeader = latitude != null && longitude != null;
		log.debug("Controller.Checking is request contains address in header. Result - " + hasCoordinatesInHeader);

		return hasCoordinatesInHeader || hasCoordinatesInBody || hasAddressInBody;
	}

}
