package telran.daily_farm.farmer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.CoordinatesDto;
import telran.daily_farm.api.dto.FarmerDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.farmer.service.IFarmer;
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

	@PostMapping(FARMER_REGISTER)
	public ResponseEntity<String> registerFarmer(@Valid @RequestBody FarmerDto farmerDto,
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
	public String login(@RequestBody LoginRequestDto loginRequestDto) {
		return farmerService.loginFarmer(loginRequestDto);
	}

	@PutMapping(FARMER_EDIT)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> updateFarmer(@Valid @RequestBody FarmerDto farmerDto,
			@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.updateFarmer(user.getId(), farmerDto);
	}

	@DeleteMapping(FARMER_REMOVE)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> removeFarmer(@AuthenticationPrincipal UserDetailsWithId user) {
		return farmerService.removeFarmer(user.getId());
	}
	
	
	
	private boolean hasValidLocation(@Valid FarmerDto farmerDto, Double latitude, Double longitude) {
			
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
