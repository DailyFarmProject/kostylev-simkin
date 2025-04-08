package telran.daily_farm.farm_set.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.farm_set.api.dto.FarmSetDto;
import telran.daily_farm.farm_set.api.dto.FarmSetRequestForCancelOrderDto;
import telran.daily_farm.farm_set.api.dto.FarmSetResponseDto;
import telran.daily_farm.farm_set.api.dto.FarmSetResponseForOrderDto;
import telran.daily_farm.farm_set.api.dto.FarmSetRequestForOrderDto;
import telran.daily_farm.farm_set.entity.FarmSet;
import telran.daily_farm.farm_set.entity.FarmSetCategory;
import telran.daily_farm.farm_set.entity.FarmSetSize;
import telran.daily_farm.farm_set.repo.FarmSetCategoryRepository;
import telran.daily_farm.farm_set.repo.FarmSetRepository;
import telran.daily_farm.farm_set.repo.FarmSetSizeRepository;
import telran.daily_farm.farmer.entity.Farmer;
import telran.daily_farm.farmer.service.FarmerService;
import telran.daily_farm.translate_service.LibreTranslateLocalService;
import telran.daily_farm.utils.servise.UtilService;

import static telran.daily_farm.api.messages.ErrorMessages.*;

@Service
@Slf4j
@AllArgsConstructor
public class FarmSetService implements IFarmSetService {

	private final FarmerService farmerService;
	private final FarmSetSizeRepository sizeRepo;
	private final FarmSetCategoryRepository categoryRepo;
	private final FarmSetRepository farmSetRepo;
	private final LibreTranslateLocalService translateService;
	private final UtilService utilService;

	@Override
	@Transactional
	public ResponseEntity<Void> addFarmSet(UUID id, FarmSetDto farmSetDto) {

		String size = farmSetDto.getSize();
		FarmSetSize farmSetSize = sizeRepo.findBySize(size)
				.orElseThrow(() -> new IllegalArgumentException(SIZE_IS_NOT_AVAILABLE));

		String category = farmSetDto.getCategory();
		FarmSetCategory farmSetCategory = categoryRepo.findByCategory(category)
				.orElseThrow(() -> new IllegalArgumentException(CATEGORY_IS_NOT_AVAILABLE));

		Farmer farmer = farmerService.getFarmer(id);

		FarmSet farmSet = FarmSet.builder().availibleCount(farmSetDto.getAvailibleCount())
				.description(farmSetDto.getDescription()).price(farmSetDto.getPrice()).farmer(farmer)
				.category(farmSetCategory).size(farmSetSize).pickupTimeEnd(farmSetDto.getPickupTimeEnd())
				.pickupTimeStart(farmSetDto.getPickupTimeStart()).build();

		farmSetRepo.save(farmSet);
		sizeRepo.findBySize(size).get().getFarmSets().add(farmSet);
		categoryRepo.findByCategory(farmSetDto.getCategory()).get().getFarmSets().add(farmSet);

		return ResponseEntity.ok().build();
	}

	@Override
	@Transactional
	public ResponseEntity<List<FarmSetResponseDto>> getAbailableFarmSetsForFarmer(UUID id) {

		List<FarmSetResponseDto> list = farmSetRepo.findByFarmerId(id).stream().map(fs -> FarmSet.buildFromEntity(fs))
				.toList();
		return translateService.translateOkResponse(ResponseEntity.ok(list), utilService.getUserLanguage(id));
	}

	@Override
	public ResponseEntity<List<FarmSetResponseDto>> getAllFarmSets() {

		List<FarmSetResponseDto> list = farmSetRepo.findAll().stream().map(fs -> FarmSet.buildFromEntity(fs)).toList();

		list.forEach(fs -> translateService.translateDto(fs, "zh"));

		return ResponseEntity.ok(list);
	}

	@Override
	@Transactional
	public ResponseEntity<FarmSetResponseForOrderDto> decreaseStock(FarmSetRequestForOrderDto farmSetRequestDto) {
		FarmSet farmSet = farmSetRepo.findById(farmSetRequestDto.getFarmSetId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Farm Set is not availible"));
		log.info("FarmSetService: decreaseStock. farmset exists - {}", farmSet.getDescription());
		if (farmSet.getAvailibleCount() == 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You ara late. Farm Set is not availible");
		log.info("FarmSetService: decreaseStock. farmset availible - {}", farmSet.getAvailibleCount());
		farmSet.setAvailibleCount(farmSet.getAvailibleCount() - 1);
		log.info("FarmSetService: decreaseStock. farmset availible after decrease - {}",  farmSet.getAvailibleCount());
	    farmSetRepo.flush();
	    log.info("FarmSetService: Saved farmSet with availibleCount = {}", farmSet.getAvailibleCount());
		return ResponseEntity.ok(FarmSetResponseForOrderDto.fromFarmSet(farmSet));
	}

	@Override
	public ResponseEntity<Void> increaseStock(FarmSetRequestForCancelOrderDto farmSetRequestDto) {
		log.info("FarmSetServoce: increaseStock.Recived FarmSetRequestForCancelOrderDto farmsetId - {}", farmSetRequestDto.getFarmSetId());
		FarmSet farmSet = farmSetRepo.findById(farmSetRequestDto.getFarmSetId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Farm Set is not availible"));
		log.info("FarmSetService: increaseStock. farmset availible - {}", farmSet.getAvailibleCount());
		farmSet.setAvailibleCount(farmSet.getAvailibleCount() + 1);
		log.info("FarmSetService: increaseStock. farmset availible after increase - {}",  farmSet.getAvailibleCount());
		farmSetRepo.save(farmSet);
	    farmSetRepo.flush();
	    log.info("FarmSetService: increaseStock. Saved farmSet with availibleCount = {}", farmSet.getAvailibleCount());
		return ResponseEntity.ok().build();
	}

}
