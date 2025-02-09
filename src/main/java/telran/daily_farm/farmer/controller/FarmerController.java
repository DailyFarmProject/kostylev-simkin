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

		if (!hasValidLocation(farmerDto, latitude, longitude)) 
		        return ResponseEntity.badRequest().body(LOCATION_REQUIRED);

		if (latitude != null && longitude != null) {
			farmerDto.setCoordinates(new CoordinatesDto(latitude, longitude));
		}

		return farmerService.registerFarmer(farmerDto);
	}

	private boolean hasValidLocation(@Valid FarmerDto farmerDto, Double latitude, Double longitude) {
		 boolean hasValidCoordinates = farmerDto.getCoordinates() != null
		            && farmerDto.getCoordinates().getLatitude() != null
		            && farmerDto.getCoordinates().getLongitude() != null;

		    boolean hasValidAddress = farmerDto.getAddress() != null
		    		&& farmerDto.getAddress().getCountry() != null
					&& farmerDto.getAddress().getCity() != null && farmerDto.getAddress().getPostalCode() != null;

		    return latitude != null && longitude != null || hasValidCoordinates || hasValidAddress;
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

}
