package telran.daily_farm.farm_set.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

import telran.daily_farm.api.dto.ApiResponse;
import telran.daily_farm.api.dto.farm_set.FarmSetDto;
import telran.daily_farm.api.dto.farm_set.FarmSetResponseDto;
import telran.daily_farm.api.dto.farm_set.GetAllLanguagesResponse;
import telran.daily_farm.api.dto.farm_set.GetFarmSetCategoryResponse;
import telran.daily_farm.api.dto.farm_set.GetFarmSetSizesResponse;

public interface IFarmSetService {

	ResponseEntity<ApiResponse<String>> addFarmSet(UUID id, FarmSetDto farmSetDto);

	ResponseEntity<ApiResponse<List<FarmSetResponseDto>>> getAbailableFarmSetsForFarmer(UUID id);

	ResponseEntity<ApiResponse<List<FarmSetResponseDto>>> getAllFarmSets();

	ResponseEntity<ApiResponse<GetAllLanguagesResponse>> getAllLanguages();

	ResponseEntity<ApiResponse<String>> changeLanguage(UUID id, String language);

	ResponseEntity<ApiResponse<GetFarmSetCategoryResponse>> getFarmSetCategories();

	ResponseEntity<ApiResponse<GetFarmSetSizesResponse>> getFarmSetSizes();

}
