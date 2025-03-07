package telran.daily_farm.farm_set.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.ApiResponse;
import telran.daily_farm.api.dto.farm_set.FarmSetDto;
import telran.daily_farm.api.dto.farm_set.FarmSetResponseDto;
import telran.daily_farm.api.dto.farm_set.GetAllLanguagesResponse;
import telran.daily_farm.api.dto.farm_set.GetFarmSetCategoryResponse;
import telran.daily_farm.api.dto.farm_set.GetFarmSetSizesResponse;
import telran.daily_farm.farm_set.service.FarmSetService;
import telran.daily_farm.security.UserDetailsWithId;

import static telran.daily_farm.api.ApiConstants.*;

@RestController
@AllArgsConstructor
@Slf4j
public class FarmSetController {

	private final FarmSetService farmSetService;

	@PostMapping(ADD_FARM_SET)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<ApiResponse<String>> addFarmSet(@Valid @RequestBody FarmSetDto farmSetDto,
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmSetService.addFarmSet(user.getId(), farmSetDto);
	}

	@GetMapping(GET_CATEGORIES)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<ApiResponse<GetFarmSetCategoryResponse>> getFarmSetCategories(
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmSetService.getFarmSetCategories();
	}

	@GetMapping(GET_SIZES)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<ApiResponse<GetFarmSetSizesResponse>> getFarmSetSizes(@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmSetService.getFarmSetSizes();
	}

	@GetMapping(GET_ALL_SETS_BY_FARMER)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<ApiResponse<List<FarmSetResponseDto>>> getAbailableFarmSetsForFarmer(
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return farmSetService.getAbailableFarmSetsForFarmer(user.getId());
	}

	
	@GetMapping(GET_ALL_SETS)
	public ResponseEntity<ApiResponse<List<FarmSetResponseDto>>> getAllFarmSets() {
		return farmSetService.getAllFarmSets();
	}
	
	@GetMapping(GET_LANGUAGES)
	public  ResponseEntity<ApiResponse<GetAllLanguagesResponse>> getAllLanguages(){
		return farmSetService.getAllLanguages();
	}
}
