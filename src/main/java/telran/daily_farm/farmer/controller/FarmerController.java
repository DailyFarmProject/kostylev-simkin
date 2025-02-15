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

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.AddressDto;
import telran.daily_farm.api.dto.ChangePasswordRequest;
import telran.daily_farm.api.dto.CoordinatesDto;
import telran.daily_farm.api.dto.FarmerRegistrationDto;
import telran.daily_farm.api.dto.FarmerUpdateDataRequestDto;
import telran.daily_farm.api.dto.FullNameDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.api.dto.TokensResponseDto;
import telran.daily_farm.farmer.service.IFarmer;
import telran.daily_farm.security.AuthService;
import telran.daily_farm.security.JwtService;
import telran.daily_farm.security.UserDetailsWithId;

import static telran.daily_farm.api.ApiConstants.*;
import static telran.daily_farm.api.messages.ErrorMessages.*;

@RestController
@Slf4j
public class FarmerController {

	@Autowired
	IFarmer farmerService;
	@Autowired
	JwtService jwtService;
	@Autowired
	private  AuthService authService;

	@PostMapping(FARMER_REGISTER)
	public ResponseEntity<String> registerFarmer(@Valid @RequestBody FarmerRegistrationDto farmerDto,
			@RequestHeader(value = "X-Latitude", required = false) Double latitude,
			@RequestHeader(value = "X-Longitude", required = false) Double longitude) {

		log.error("Controller. Request for registration new farmer - " + farmerDto.getEmail());
		if (!hasValidLocation(farmerDto, latitude, longitude)) 
		        return ResponseEntity.badRequest().body(LOCATION_REQUIRED);
		
		
		
		if (latitude != null && longitude != null && farmerDto.getCoordinates() == null) {
			farmerDto.setCoordinates(new CoordinatesDto(latitude, longitude));
			log.debug("Controller. There is not coordinates in body. Update coordinates in dto from header");
		}
		return farmerService.registerFarmer(farmerDto);
	}




	@PostMapping(FARMER_LOGIN)
	public ResponseEntity<TokensResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
		return farmerService.loginFarmer(loginRequestDto);
	}
	
	
	/////////////////////////////////////////////////////////////////////////
	@GetMapping(FARMER_REFRESH_TOKEN)
	public String refresh(@RequestBody String refreshToken) {
		log.info("Controller/ refresh token starts");
	        return authService.refreshAccessToken(refreshToken);
	    }

	@PutMapping(FARMER_EDIT)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<TokensResponseDto> updateFarmer(@Valid @RequestBody FarmerUpdateDataRequestDto farmerDto,
			@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.updateFarmer(user.getId(), farmerDto);
	}

	@DeleteMapping(FARMER_REMOVE)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> removeFarmer(@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.removeFarmer(user.getId());
	}
	
	//TODO
	@PutMapping(FARMER_CHANGE_PASSWORD)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<TokensResponseDto> farmerUpdatePassword(@Valid @RequestBody ChangePasswordRequest changePasswordDto,
			@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.updatePassword(user.getId(), changePasswordDto);
	}
	
	//TODO
	@PutMapping(FARMER_CHANGE_ADDRESS)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> farmerUpdateAddress(@Valid @RequestBody AddressDto addressDto,
			@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.updateAddress(user.getId(), addressDto);
	}
	
	//TODO
	@PutMapping(FARMER_CHANGE_COORDINATES)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> farmerUpdateCoordinates(@Valid @RequestBody CoordinatesDto coordinatesDto,
			@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.updateCoordinates(user.getId(), coordinatesDto);
	}
	
	//TODO
	@PutMapping(FARMER_CHANGE_EMAIL)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<TokensResponseDto> farmerUpdateEmail(@Valid @RequestBody String newEmail,
			@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.updateEmail(user.getId(), newEmail);
	}
	
	//TODO
	@PutMapping(FARMER_CHANGE_PHONE)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> farmerUpdatePhone(@Valid @RequestBody String newPhone,
			@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.updatePhone(user.getId(), newPhone);
	}
	
	//TODO
	@PreAuthorize("hasRole(ROLE_FARMER)")
	@PutMapping(FARMER_CHANGE_FIRST_LAST_NAME_PASSWORD)
	public ResponseEntity<String> farmerUpdateName(@Valid @RequestBody FullNameDto fullname,
			@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.changeName(user.getId(), fullname);
	}
	
	
	
	
	
	private boolean hasValidLocation(@Valid FarmerRegistrationDto farmerDto, Double latitude, Double longitude) {
			
		 	boolean hasCoordinatesInBody = farmerDto.getCoordinates() != null
		            && farmerDto.getCoordinates().getLatitude() != null
		            && farmerDto.getCoordinates().getLongitude() != null;
		 	log.debug("Controller. Checking is request contains coordinate in body. Result - "+ hasCoordinatesInBody );
		 	
		    boolean hasAddressInBody = farmerDto.getAddress() != null
		    		&& farmerDto.getAddress().getCountry() != null
					&& farmerDto.getAddress().getCity() != null && farmerDto.getAddress().getPostalCode() != null;
		    log.debug("Controller. Checking is request contains address in body. Result - "+ hasAddressInBody );
		    
		    boolean hasCoordinatesInHeader = latitude != null && longitude != null;
		    log.debug("Controller.Checking is request contains address in header. Result - "+ hasCoordinatesInHeader );
		    
		    return  hasCoordinatesInHeader || hasCoordinatesInBody || hasAddressInBody;
	}

}
