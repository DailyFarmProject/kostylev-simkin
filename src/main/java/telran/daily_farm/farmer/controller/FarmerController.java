package telran.daily_farm.farmer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.customer.api.dto.UpdatePhoneRequestDto;
import telran.daily_farm.farmer.api.dto.CoordinatesDto;
import telran.daily_farm.farmer.api.dto.FarmerUpdateDataRequestDto;
import telran.daily_farm.farmer.api.dto.UpdateCompanyRequestDto;
import telran.daily_farm.farmer.service.IFarmer;
import telran.daily_farm.security.UserDetailsWithId;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import static telran.daily_farm.api.ApiConstants.*;

@Tag(name = "Farmer API", description = "Methods for farmer")
@RestController
@Slf4j
public class FarmerController {

	@Autowired
	IFarmer farmerService;



//	@PostMapping(FARMER_REGISTER)
//	public ResponseEntity<String> registerFarmer(@Valid @RequestBody FarmerRegistrationDto farmerDto,
//			@RequestHeader(value = "X-Latitude", required = false) Double latitude,
//			@RequestHeader(value = "X-Longitude", required = false) Double longitude,
//			@RequestHeader(value = "Accept-Language", defaultValue = "en") String browserLanguage,
//			@RequestHeader(value = "X-User-Language", required = false) String userLanguage) {
//
//		log.info("Controller. Request for registration new farmer - " + farmerDto.getEmail());
//		if (latitude == null && longitude == null && farmerDto.getCoordinates() == null)
//			return ResponseEntity.badRequest().body(LOCATION_REQUIRED);
//
//		if (latitude != null && longitude != null && farmerDto.getCoordinates() == null) {
//			farmerDto.setCoordinates(new CoordinatesDto(latitude, longitude));
//			log.debug("Controller. There is not coordinates in body. Update coordinates in dto from header");
//		}
//		
//		
//		String language;
//		if(userLanguage == null || userLanguage.isBlank())
//			language = browserLanguage != null ? browserLanguage.split(",")[0] : "en";
//		else
//			language = userLanguage;
//			
//		return farmerService.registerFarmer(farmerDto, language);
//	}
//	

//	@GetMapping(FARMER_EMAIL_VERIFICATION)
//	public ResponseEntity<String> emailVerification(@RequestParam String token){
//		log.info("Controller. Email verification starts - " + token);
//		return farmerService.emailVerification(token);
//		
//	}
	
//	@GetMapping(FARMER_EMAIL_VERIFICATION_RESEND)
//	public ResponseEntity<String> resendVerificationLink(@Valid @RequestBody SendToRequestDto sendToRequestDto){
//		log.info("Controller. Resend verification link");
//		return farmerService.resendVerificationLink(sendToRequestDto.getEmail());
//		
//	}
	
	
//	@GetMapping(RESET_PASSWORD)
//	public ResponseEntity<String> generateAndSendNewPassword(@Valid @RequestBody SendToRequestDto sendToRequestDto){
//		log.info("Controller. generateAndSendNewPassword starts - " + sendToRequestDto.getEmail());
//		return farmerService.generateAndSendNewPassword(sendToRequestDto.getEmail());
//		
//	}
	
	
//	@DeleteMapping(FARMER_LOGOUT)
//	public ResponseEntity<String> logoutFarmer(	@AuthenticationPrincipal UserDetailsWithId user,
//			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
//		farmerService.logoutFarmer(user.getId(), token);
//		return ResponseEntity.ok(LOGOUT_SUCCESS);
//	}


//	@PostMapping(FARMER_LOGIN)
//	public ResponseEntity<TokensResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
//		return farmerService.loginFarmer(loginRequestDto);
//	}

//	@GetMapping(FARMER_REFRESH_TOKEN)
//	public ResponseEntity<RefreshTokenResponseDto> refresh(
//			@Parameter(description = "JWT токен", required = true) @RequestBody RefreshTokenRequestDto request) {
//		log.info("Controller/ refresh token starts");
//		return authService.refreshAccessTokenFarmer(request.getRefreshToken());
//	}

	@PutMapping(FARMER_EDIT)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> updateFarmer(@Valid @RequestBody FarmerUpdateDataRequestDto farmerDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updateFarmer(user.getId(), farmerDto);
	}

//	@DeleteMapping(FARMER_REMOVE)
//	@PreAuthorize("hasRole(ROLE_FARMER)")
//	public ResponseEntity<String> removeFarmer(@AuthenticationPrincipal UserDetailsWithId user,
//			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
//		return farmerService.removeFarmer(user.getId());
//	}

//	@PutMapping(FARMER_CHANGE_PASSWORD)
//	@PreAuthorize("hasRole(ROLE_FARMER)")
//	public ResponseEntity<TokensResponseDto> farmerUpdatePassword(
//			@Valid @RequestBody ChangePasswordRequest changePasswordDto,
//			@AuthenticationPrincipal UserDetailsWithId user,
//			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
//		return farmerService.updatePassword(user.getId(), changePasswordDto);
//	}
//	
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
	


	@PutMapping(FARMER_CHANGE_PHONE)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> farmerUpdatePhone(@Valid @RequestBody UpdatePhoneRequestDto updatePhoneDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmerService.updatePhone(user.getId(), updatePhoneDto.getPhone());
	}
	
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
