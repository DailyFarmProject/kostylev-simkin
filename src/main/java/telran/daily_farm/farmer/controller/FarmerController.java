package telran.daily_farm.farmer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.farmer.api.dto.*;
import telran.daily_farm.farmer.service.IFarmer;
import telran.daily_farm.security.UserDetailsWithId;
import telran.daily_farm.translate_service.ITranslateService;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import static telran.daily_farm.farmer.api.FarmerApiConstants.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FarmerController {

	
	private final IFarmer farmerService;
	private final ITranslateService translateService;
	

	@PutMapping(FARMER_EDIT)
	@PreAuthorize("hasRole('ROLE_FARMER')")
	public ResponseEntity<String> updateFarmer(@Valid @RequestBody FarmerUpdateDataRequestDto farmerDto,
			@AuthenticationPrincipal UserDetailsWithId user, @RequestHeader("Authorization") String token) {
		return farmerService.updateFarmer(user.getId(), farmerDto);
	}

	@PutMapping(FARMER_CHANGE_COORDINATES)
	@PreAuthorize("hasRole('ROLE_FARMER')")
	public ResponseEntity<String> farmerUpdateCoordinates(@Valid @RequestBody CoordinatesDto coordinatesDto,
			@RequestHeader(value = "X-Latitude", required = false) Double latitude,
			@RequestHeader(value = "X-Longitude", required = false) Double longitude,
			@AuthenticationPrincipal UserDetailsWithId user, @RequestHeader("Authorization") String token) {

		if ((latitude == null && longitude == null) && coordinatesDto == null)
			return ResponseEntity.badRequest().body(LOCATION_REQUIRED);
		if (coordinatesDto == null)
			coordinatesDto = new CoordinatesDto(latitude, longitude);

		return farmerService.updateCoordinates(user.getId(), coordinatesDto);
	}

	@PutMapping(FARMER_CHANGE_PHONE)
	@PreAuthorize("hasRole('ROLE_FARMER')")
	public ResponseEntity<String> farmerUpdatePhone(@Valid @RequestBody UpdatePhoneRequestDto updatePhoneDto,
			@AuthenticationPrincipal UserDetailsWithId user, @RequestHeader("Authorization") String token) {
		return farmerService.updatePhone(user.getId(), updatePhoneDto.getPhone());
	}

	@PutMapping(FARMER_CHANGE_COMPANY_NAME)
	@PreAuthorize("hasRole('ROLE_FARMER')")
	public ResponseEntity<String> farmerUpdateCompany(@Valid @RequestBody UpdateCompanyRequestDto updateCompanyDto,
			@AuthenticationPrincipal UserDetailsWithId user, @RequestHeader("Authorization") String token) {
		return farmerService.updateCompany(user.getId(), updateCompanyDto.getCompany());
	}
	

	@PostMapping(CHANGE_USER_LANGUAGE)
	@PreAuthorize("hasRole('ROLE_FARMER')")
	public ResponseEntity<Void> changeLanguage(@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token,
			@Valid @RequestBody UpdateLanguageRequestDto updateLanguageDto) {
		log.info("changeLanguage to - {}",updateLanguageDto.getNewLanguage() );
		
		if(!translateService.getAllLanguages().keySet().contains(updateLanguageDto.getNewLanguage()))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Application does not support current language");
			
		
		
		return farmerService.changeLanguage(user.getId(), updateLanguageDto.getNewLanguage());
	}



}
