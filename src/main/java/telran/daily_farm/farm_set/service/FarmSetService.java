package telran.daily_farm.farm_set.service;


import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.farm_set.api.dto.FarmSetDto;
import telran.daily_farm.farm_set.api.dto.FarmSetResponseDto;
import telran.daily_farm.farm_set.entity.FarmSet;
import telran.daily_farm.farm_set.entity.FarmSetCategory;
import telran.daily_farm.farm_set.entity.FarmSetSize;
import telran.daily_farm.farm_set.repo.FarmSetCategoryRepository;
import telran.daily_farm.farm_set.repo.FarmSetRepository;
import telran.daily_farm.farm_set.repo.FarmSetSizeRepository;
import telran.daily_farm.farmer.repo.FarmerRepository;
import telran.daily_farm.translate_service.LibreTranslateLocalService;
import telran.daily_farm.utils.servise.UtilService;

import static telran.daily_farm.api.messages.ErrorMessages.*;

@Service
@Slf4j
@AllArgsConstructor
public class FarmSetService  implements IFarmSetService{

	private final FarmerRepository farmerRepo;
	private final FarmSetSizeRepository sizeRepo;
	private final FarmSetCategoryRepository categoryRepo;
	private final FarmSetRepository farmSetRepo;
	private final LibreTranslateLocalService translateService; 
	private final UtilService utilService;
	
	
	@Override
	@Transactional
	public ResponseEntity<Void> addFarmSet(UUID id, FarmSetDto farmSetDto) {
		
		String size = farmSetDto.getSize();
		FarmSetSize farmSetSize = sizeRepo.findBySize(size).orElseThrow(() ->
			new IllegalArgumentException(SIZE_IS_NOT_AVAILABLE));
			
		
		String category = farmSetDto.getCategory();
		FarmSetCategory farmSetCategory = categoryRepo.findByCategory(category).orElseThrow(()->
			new IllegalArgumentException( CATEGORY_IS_NOT_AVAILABLE)) ;
		
		FarmSet farmSet = FarmSet.builder()
				.availibleCount(farmSetDto.getAvailibleCount())
//				.abailible(true)
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
		
		
		return ResponseEntity.ok().build();
	}




	@Override
	@Transactional
	public ResponseEntity<List<FarmSetResponseDto>> getAbailableFarmSetsForFarmer(UUID id) {
		List<FarmSetResponseDto> list = farmerRepo.findByid(id).get().getFarmSets().stream().map(fs->FarmSet.buildFromEntity(fs)).toList();
		return translateService.translateOkResponse(ResponseEntity.ok(list), utilService.getUserLanguage(id));
	}


	@Override
	public ResponseEntity<List<FarmSetResponseDto>> getAllFarmSets() {
		
		List<FarmSetResponseDto> list = farmSetRepo.findAll().stream().map(fs->FarmSet.buildFromEntity(fs)).toList();
		
		list.forEach(fs->translateService.translateDto(fs, "zh"));
				
		return ResponseEntity.ok(list);
	}




}
