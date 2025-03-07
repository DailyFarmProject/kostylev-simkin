package telran.daily_farm.farm_set.service;


import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.entity.farm_set.FarmSet;
import telran.daily_farm.entity.farm_set.FarmSetCategory;
import telran.daily_farm.entity.farm_set.FarmSetSize;
import telran.daily_farm.farm_set.repo.FarmSetCategoryRepository;
import telran.daily_farm.farm_set.repo.FarmSetRepository;
import telran.daily_farm.farm_set.repo.FarmSetSizeRepository;
import telran.daily_farm.api.dto.ApiResponse;
import telran.daily_farm.api.dto.farm_set.*;
import telran.daily_farm.farmer.repo.FarmerRepository;
import telran.daily_farm.translate_service.LibreTranslateLocalService;

import static telran.daily_farm.api.messages.ErrorMessages.*;
import static telran.daily_farm.api.messages.SuccessMessages.*;

@Service
@Slf4j
@AllArgsConstructor
public class FarmSetService  implements IFarmSetService{

	private final FarmerRepository farmerRepo;
	private final FarmSetSizeRepository sizeRepo;
	private final FarmSetCategoryRepository categoryRepo;
	private final FarmSetRepository farmSetRepo;
	private final LibreTranslateLocalService translateService; 
	
	
	@Override
	@Transactional
	public ResponseEntity<ApiResponse<String>> addFarmSet(UUID id, FarmSetDto farmSetDto) {
		
		String size = farmSetDto.getSize();
		FarmSetSize farmSetSize = sizeRepo.findBySize(size).orElseThrow(() ->
			new IllegalArgumentException(SIZE_IS_NOT_AVAILABLE));
			
		
		String category = farmSetDto.getCategory();
		FarmSetCategory farmSetCategory = categoryRepo.findByCategory(category).orElseThrow(()->
			new IllegalArgumentException( CATEGORY_IS_NOT_AVAILABLE)) ;
		
		FarmSet farmSet = FarmSet.builder()
				.availibleCount(farmSetDto.getAvailibleCount())
				.abailible(true)
				.description(farmSetDto.getDescription())
				.price(farmSetDto.getPrice())
				.farmer(farmerRepo.findByid(id).get())
				.category(farmSetCategory)
				.size(farmSetSize)
				.pickupTimeEnd(farmSetDto.getPickupTimeEnd())
				.pickupTimeStart(farmSetDto.getPickupTimeStart())
				.build();
		
		farmSetRepo.save(farmSet);
		sizeRepo.findBySize(size).get().getFarmSets().add(farmSet);
		categoryRepo.findByCategory(farmSetDto.getCategory()).get().getFarmSets().add(farmSet);
		
		ApiResponse<String> response = ApiResponse.success(null, FARM_SET_ADDED_SUCCESSFULY);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<ApiResponse<GetFarmSetCategoryResponse>> getFarmSetCategories() {
		GetFarmSetCategoryResponse res = new GetFarmSetCategoryResponse(categoryRepo.findAll().stream().map(el->el.getCategory()).toList());
		ApiResponse<GetFarmSetCategoryResponse> response = ApiResponse.success(res, "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<ApiResponse<GetFarmSetSizesResponse>>getFarmSetSizes() {
		GetFarmSetSizesResponse res = (new GetFarmSetSizesResponse(sizeRepo.findAll().stream().map(el->el.getSize()).toList()));
		ApiResponse<GetFarmSetSizesResponse> response = ApiResponse.success(res, "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Override
	@Transactional
	public ResponseEntity<ApiResponse<List<FarmSetResponseDto>>> getAbailableFarmSetsForFarmer(UUID id) {
		List<FarmSetResponseDto> list = farmerRepo.findByid(id).get().getFarmSets().stream().map(fs->FarmSet.buildFromEntity(fs)).toList();
		ApiResponse<List<FarmSetResponseDto>> response = ApiResponse.success(list, "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<ApiResponse<List<FarmSetResponseDto>>> getAllFarmSets() {
		
		List<FarmSetResponseDto> list = farmSetRepo.findAll().stream().map(fs->FarmSet.buildFromEntity(fs)).toList();
		ApiResponse<List<FarmSetResponseDto>> response = ApiResponse.success(list, "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<ApiResponse<GetAllLanguagesResponse>> getAllLanguages() {
		GetAllLanguagesResponse res =  new GetAllLanguagesResponse(translateService.getAllLanguages());
		ApiResponse<GetAllLanguagesResponse> response = ApiResponse.success(res, "");
		return new ResponseEntity<>(response, HttpStatus.OK);
	}


	@Override
	public ResponseEntity<ApiResponse<String>> changeLanguage(UUID id, String language) {
		ApiResponse<String> response = ApiResponse.success(null, LANGUAGE_CHANGED);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

}
