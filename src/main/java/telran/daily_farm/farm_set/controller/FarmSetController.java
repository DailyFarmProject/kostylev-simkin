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

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.farm_set.api.dto.FarmSetDto;
import telran.daily_farm.farm_set.api.dto.FarmSetResponseDto;
import telran.daily_farm.farm_set.service.FarmSetService;
import telran.daily_farm.security.UserDetailsWithId;

import static telran.daily_farm.farm_set.api.FarmSetApiConstants.*;

@RestController
@AllArgsConstructor
@Slf4j
public class FarmSetController {

	private final FarmSetService farmSetService;

	@PostMapping(ADD_FARM_SET)
	@PreAuthorize("hasRole('ROLE_FARMER')")
	public ResponseEntity<Void> addFarmSet(@Valid @RequestBody FarmSetDto farmSetDto,
			@AuthenticationPrincipal UserDetailsWithId user,
		@RequestHeader("Authorization") String token) {
		return farmSetService.addFarmSet(user.getId(), farmSetDto);
	}


	@GetMapping(GET_ALL_SETS_BY_FARMER)
	@PreAuthorize("hasRole('ROLE_FARMER')")
	public ResponseEntity<List<FarmSetResponseDto>> getAbailableFarmSetsForFarmer(
			@AuthenticationPrincipal UserDetailsWithId user,
		 @RequestHeader("Authorization") String token) {
		return farmSetService.getAbailableFarmSetsForFarmer(user.getId());
	}

	
	@GetMapping(GET_ALL_SETS)
	public ResponseEntity<List<FarmSetResponseDto>> getAllFarmSets() {
		return farmSetService.getAllFarmSets();
	}
	

}
