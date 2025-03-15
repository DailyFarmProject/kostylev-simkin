package telran.daily_farm.farm_set.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import telran.daily_farm.api.dto.farm_set.FarmSetDto;
import telran.daily_farm.api.dto.farm_set.FarmSetResponseDto;

public interface IFarmSetService {

	ResponseEntity<Void> addFarmSet(UUID id, FarmSetDto farmSetDto);

	ResponseEntity<List<FarmSetResponseDto>> getAbailableFarmSetsForFarmer(UUID id);

	ResponseEntity<List<FarmSetResponseDto>> getAllFarmSets();



}
