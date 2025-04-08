package telran.daily_farm.farm_set.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import telran.daily_farm.farm_set.api.dto.FarmSetDto;
import telran.daily_farm.farm_set.api.dto.FarmSetRequestForCancelOrderDto;
import telran.daily_farm.farm_set.api.dto.FarmSetResponseDto;
import telran.daily_farm.farm_set.api.dto.FarmSetResponseForOrderDto;
import telran.daily_farm.farm_set.api.dto.FarmSetRequestForOrderDto;

public interface IFarmSetService {

	ResponseEntity<Void> addFarmSet(UUID id, FarmSetDto farmSetDto);

	ResponseEntity<List<FarmSetResponseDto>> getAbailableFarmSetsForFarmer(UUID id);

	ResponseEntity<List<FarmSetResponseDto>> getAllFarmSets();

	ResponseEntity<FarmSetResponseForOrderDto> decreaseStock(FarmSetRequestForOrderDto farmSetRequestDto);

	ResponseEntity<Void> increaseStock(FarmSetRequestForCancelOrderDto farmSetRequestDto);



}
