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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.ChangePasswordRequest;
import telran.daily_farm.api.dto.CoordinatesDto;
import telran.daily_farm.api.dto.FarmerRegistrationDto;
import telran.daily_farm.api.dto.FarmerUpdateDataRequestDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.api.dto.RefreshTokenRequestDto;
import telran.daily_farm.api.dto.RefreshTokenResponseDto;
import telran.daily_farm.api.dto.SendToRequestDto;
import telran.daily_farm.api.dto.TokensResponseDto;
import telran.daily_farm.api.dto.UpdateCompanyRequestDto;
import telran.daily_farm.api.dto.UpdatePhoneRequestDto;
import telran.daily_farm.farmer.service.IFarmer;
import telran.daily_farm.security.AuthService;
import telran.daily_farm.security.JwtService;
import telran.daily_farm.security.UserDetailsWithId;

import static telran.daily_farm.api.messages.ErrorMessages.*;
import static telran.daily_farm.api.messages.SuccessMessages.*;

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


	@Operation(summary = "Registration of new Farmer",
				description = "Registration of new Farmer", 
				requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
							description = "Registration data", 
							content = @Content(mediaType = "application/json", schema = @Schema(implementation = FarmerRegistrationDto.class))))
	@PostMapping(FARMER_REGISTER)
	public ResponseEntity<String> registerFarmer(@Valid @RequestBody FarmerRegistrationDto farmerDto,
			@RequestHeader(value = "X-Latitude", required = false) Double latitude,
			@RequestHeader(value = "X-Longitude", required = false) Double longitude,
			@RequestHeader(value = "Accept-Language", defaultValue = "en") String browserLanguage,
			@RequestHeader(value = "X-User-Language", required = false) String userLanguage) {

		log.info("Controller. Request for registration new farmer - " + farmerDto.getEmail());
		if (latitude == null && longitude == null && farmerDto.getCoordinates() == null)
			return ResponseEntity.badRequest().body(LOCATION_REQUIRED);

		if (latitude != null && longitude != null && farmerDto.getCoordinates() == null) {
			farmerDto.setCoordinates(new CoordinatesDto(latitude, longitude));
			log.debug("Controller. There is not coordinates in body. Update coordinates in dto from header");
		}
		
		
		String language;
		if(userLanguage == null || userLanguage.isBlank())
			language = browserLanguage != null ? browserLanguage.split(",")[0] : "en";
		else
			language = userLanguage;
			
		return farmerService.registerFarmer(farmerDto, language);
	}
	
	@Operation(summary = "Email verification", 
				description = "Email verification. Send link to farmer's email with verification token(token is expired after 5 minutes). User need to follow the link for verification(set credential is activated field to true)", 
				requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
						description = "Verification token", 
						content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@GetMapping(FARMER_EMAIL_VERIFICATION)
	public ResponseEntity<String> emailVerification(@RequestParam String token){
		log.info("Controller. Email verification starts - " + token);
		return farmerService.emailVerification(token);
		
	}
	
	@Operation(summary = "Resend verification link",
			description = "If the verification link is expired it's can be resend to email by this request",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Resend verification token", 
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@GetMapping(FARMER_EMAIL_VERIFICATION_RESEND)
	public ResponseEntity<String> resendVerificationLink(@Valid @RequestBody SendToRequestDto sendToRequestDto){
		log.info("Controller. Resend verification link");
		return farmerService.resendVerificationLink(sendToRequestDto.getEmail());
		
	}
	
	@Operation(summary = "Reset user's password", description = "If user forgot password, request generate new password and send it to email",
				requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
						description = "Resend verification token", 
						content = @Content(mediaType = "application/json", schema = @Schema(implementation = SendToRequestDto.class))))
	@GetMapping(RESET_PASSWORD)
	public ResponseEntity<String> generateAndSendNewPassword(@Valid @RequestBody SendToRequestDto sendToRequestDto){
		log.info("Controller. generateAndSendNewPassword starts - " + sendToRequestDto.getEmail());
		return farmerService.generateAndSendNewPassword(sendToRequestDto.getEmail());
		
	}
	
	
	
	@Operation(summary = "Logout of Farmer",
			description = "Logout of Farmer. Removes refreshToken and adds accessToken to blacklist", 
			security = @SecurityRequirement(name = "Bearer Authentication"),
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Data for logout", 
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@DeleteMapping(FARMER_LOGOUT)
	public ResponseEntity<String> logoutFarmer(	@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		farmerService.logoutFarmer(user.getId(), token);
		return ResponseEntity.ok(LOGOUT_SUCCESS);
	}

	@Operation(summary = "Login of Farmer",
			description = "Login. Return accessToken and refreshToken", 
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Data for login", 
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = LoginRequestDto.class))))
	@PostMapping(FARMER_LOGIN)
	public ResponseEntity<TokensResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
		return farmerService.loginFarmer(loginRequestDto);
	}

	@Operation(summary = "Refresh token",
			description = "When accessToken expired any request thows exception. Need to refresh token by sending refreshToken to this endpoint. Return new  accessToken", 
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Refresh Token", 
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshTokenRequestDto.class))))
	@GetMapping(FARMER_REFRESH_TOKEN)
	public ResponseEntity<RefreshTokenResponseDto> refresh(
			@Parameter(description = "JWT токен", required = true) @RequestBody RefreshTokenRequestDto request) {
		log.info("Controller/ refresh token starts");
		return authService.refreshAccessTokenFarmer(request.getRefreshToken());
	}

	@Operation(summary = "Update phone, company and coordinates by one request", 
			description = "Option to update all (all not requared) data with sending one form.", 
			security = @SecurityRequirement(name = "Bearer Authentication"),
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Update data", 
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = FarmerUpdateDataRequestDto.class))))
	@PutMapping(FARMER_EDIT)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> updateFarmer(@Valid @RequestBody FarmerUpdateDataRequestDto farmerDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updateFarmer(user.getId(), farmerDto);
	}

	@Operation(summary = "Remove Farmer", 
			description = "Remove Farmer from database", 
			security = @SecurityRequirement(name = "Bearer Authentication"), 
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@DeleteMapping(FARMER_REMOVE)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> removeFarmer(@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.removeFarmer(user.getId());
	}

	@Operation(summary = "Update password",
			description = "Update password. Return new accessToken and new refreshToken",
			security = @SecurityRequirement(name = "Bearer Authentication"),
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = ChangePasswordRequest.class))))
	@PutMapping(FARMER_CHANGE_PASSWORD)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<TokensResponseDto> farmerUpdatePassword(
			@Valid @RequestBody ChangePasswordRequest changePasswordDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updatePassword(user.getId(), changePasswordDto);
	}
	

	@Operation(summary = "Update Farmer's coordinates",
			description = "Update Farmer's coordinates. New coordinates can be send in header or in body ",
			security = @SecurityRequirement(name = "Bearer Authentication"), 
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = CoordinatesDto.class))))
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
	
	@Operation(summary = "Send link to verify updating email", 
			description = "If user want's to change email server send link that contains token. User must to follow this link. "
			+ "Token is expired after 5 minutes. Token can be used one time. Ater that it's in black list.",
			security = @SecurityRequirement(name = "Bearer Authentication"), 
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Email",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = SendToRequestDto.class))))
	@PreAuthorize("hasRole(ROLE_FARMER)")
	@GetMapping(FARMER_EMAIL_CHANGE_VERIFICATION)
	public ResponseEntity<String> sendVerificationTokenForUpdateEmail(@Valid @RequestBody SendToRequestDto sendToRequestDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token){
		log.info("Controller. getEmailUpdateToken starts - " + sendToRequestDto.getEmail());
		return farmerService.sendVerificationTokenForUpdateEmail(user.getId(), sendToRequestDto.getEmail());
		
	}
	
	@Operation(summary = "Send link to verify new email (when updating email)", 
			description = "After verification of changing email server send link that contains token to new email. User must to follow this link. "
			+ "Token is expired after 5 minutes. Token can be used one time. Ater that it's in black list.",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					description = "Email",
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@GetMapping(FARMER_NEW_EMAIL_VERIFICATION)
	public ResponseEntity<String> getVerificationTokenToNewEmail(@RequestParam  String token){
		log.info("Controller. getVerificationTokenToNewEmail starts - " + token);
		return farmerService.sendVerificationTokenToNewEmail(token);
		
	}

	@Operation(summary = "Update Farmer's email",
			description = "Get token from new email. Extract new email from token and change email. User need to login",
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = String.class))))
	@GetMapping(FARMER_CHANGE_EMAIL)
	public ResponseEntity<String> farmerUpdateEmail(@RequestParam String token) {
		return farmerService.updateEmail(token);
	}

	@Operation(summary = "Update Farmer's phone number",
			description = "Update Farmer's phone number", 
			security = @SecurityRequirement(name = "Bearer Authentication"), 
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdatePhoneRequestDto.class))))
	@PutMapping(FARMER_CHANGE_PHONE)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> farmerUpdatePhone(@Valid @RequestBody UpdatePhoneRequestDto updatePhoneDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updatePhone(user.getId(), updatePhoneDto.getPhone());
	}
	
	@Operation(summary = "Update Farmer's company name",
			description = "Update Farmer's company name",
			security = @SecurityRequirement(name = "Bearer Authentication"),
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
					content = @Content(mediaType = "application/json", schema = @Schema(implementation = UpdateCompanyRequestDto.class))))
	@PutMapping(FARMER_CHANGE_COMPANY_NAME)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> farmerUpdateCompany(@Valid @RequestBody UpdateCompanyRequestDto updateCompanyDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updateCompany(user.getId(), updateCompanyDto.getCompany());
	}

	
	
	
//	@Operation(summary = "Update Farmer's address", description = "Update Farmer's address.", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Данные нового адреса", content = @Content(mediaType = "application/json", schema = @Schema(implementation = AddressDto.class))))
//	@PutMapping(FARMER_CHANGE_ADDRESS)
//	@PreAuthorize("hasRole(ROLE_FARMER)")
//	public ResponseEntity<String> farmerUpdateAddress(@Valid @RequestBody AddressDto addressDto,
//			@AuthenticationPrincipal UserDetailsWithId user,
//			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
//		return farmerService.updateAddress(user.getId(), addressDto);
//	}
	
//	@Operation(summary = "Update Farmer's first name and last name", description = "Update Farmer's first name and last name", security = @SecurityRequirement(name = "Bearer Authentication"), requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(mediaType = "application/json", schema = @Schema(implementation = FullNameDto.class))))
//	@PreAuthorize("hasRole(ROLE_FARMER)")
//	@PutMapping(FARMER_CHANGE_FIRST_LAST_NAME_PASSWORD)
//	public ResponseEntity<String> farmerUpdateName(@Valid @RequestBody FullNameDto fullname,
//			@AuthenticationPrincipal UserDetailsWithId user,
//			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
//		return farmerService.changeName(user.getId(), fullname);
//	}

//	private boolean hasValidLocation(@Valid FarmerRegistrationDto farmerDto, Double latitude, Double longitude) {
//
//		
//		boolean hasCoordinatesInBody = farmerDto.getCoordinates() != null
//				&& farmerDto.getCoordinates().getLatitude() != null
//				&& farmerDto.getCoordinates().getLongitude() != null;
//		log.debug("Controller. Checking is request contains coordinate in body. Result - " + hasCoordinatesInBody);
//
//		boolean hasAddressInBody = farmerDto.getAddress() != null && farmerDto.getAddress().getCountry() != null
//				&& farmerDto.getAddress().getCity() != null && farmerDto.getAddress().getPostalCode() != null;
//		log.debug("Controller. Checking is request contains address in body. Result - " + hasAddressInBody);
//
//		boolean hasCoordinatesInHeader = latitude != null && longitude != null;
//		log.debug("Controller.Checking is request contains address in header. Result - " + hasCoordinatesInHeader);
//
//		return hasCoordinatesInHeader || hasCoordinatesInBody || hasAddressInBody;
//	}

}
