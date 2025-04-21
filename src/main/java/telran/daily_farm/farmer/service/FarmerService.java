package telran.daily_farm.farmer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.farmer.api.dto.*;
import telran.daily_farm.farmer.entity.Coordinates;
import telran.daily_farm.farmer.entity.Farmer;
import telran.daily_farm.farmer.repo.CoordinatesRepository;
import telran.daily_farm.farmer.repo.FarmerRepository;
import telran.daily_farm.location.service.ILocationService;
import telran.daily_farm.translate_service.LibreTranslateLocalService;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class FarmerService implements IFarmer {

	private final FarmerRepository farmerRepo;
	private final CoordinatesRepository coordinatesRepo;
	private final LibreTranslateLocalService libreTranslate;
	private final StringRedisTemplate redisTemplate;
	//private final AddressRepository addressRepo;


	@Autowired
	ILocationService locationService;
	
	 @Value("${jwt.refresh.token.validity}")
	 private long languageCacheValidity ;


	@Override
	@Transactional
	public ResponseEntity<String> updateFarmer(UUID id, FarmerUpdateDataRequestDto farmerDto) {
		log.info("Service. Update farmer data starts");

		Farmer farmer = farmerRepo.findByid(id).get();
		farmer.setPhone(farmerDto.getPhone());
		log.info("Service. Phone updated");
		farmer.setCompany(farmerDto.getCompany());
		log.info("Service. Phone updated");
		coordinatesRepo.findByFarmer(new Farmer(id)).updateFromDto(farmerDto.getCoordinates());
		log.info("coordinates updated successfully");

		return ResponseEntity.ok("Data updated successfuly");
	}

//	@Override
//	@Transactional
//	public ResponseEntity<String> updateAddress(UUID id, AddressDto addressDto) {
//
//		addressRepo.findByFarmer(new Farmer(id)).updateFromDto(addressDto);
//		coordinatesRepo.findByFarmer(new Farmer(id))
//				.updateFromDto(locationService.getCoordinatesFromAddress(addressDto));
//
//		return ResponseEntity.ok("Coordinates and address updated successfully");
//	}

	@Override
	@Transactional
	public ResponseEntity<String> updateCoordinates(UUID id, CoordinatesDto coordinatesDto) {
		coordinatesRepo.findByFarmer(new Farmer(id)).updateFromDto(coordinatesDto);
//		addressRepo.findByFarmer(new Farmer(id))
//				.updateFromDto(locationService.getAddtessFromCoordinates(coordinatesDto));
		return ResponseEntity.ok("Coordinates and address updated successfully");
	}
	

	@Override
	@Transactional
	public ResponseEntity<String> updatePhone(UUID id, String newPhone) {
		farmerRepo.findByid(id).get().setPhone(newPhone);
		return ResponseEntity.ok(libreTranslate.translate("","","Phone updated successfully"));

	}

	
	@Override
	@Transactional
	public ResponseEntity<String> updateCompany(UUID id, String company) {
		farmerRepo.findByid(id).get().setCompany(company);
		return ResponseEntity.ok("Company updated successfully");
	}
	
	
	@Override
	public void createFarmer(Farmer farmer, CoordinatesDto coordinatesDto, String lang) {
		Coordinates coordinates = Coordinates.of(coordinatesDto);
		coordinates.setFarmer(farmer);
		farmer.setBalance(0.);
		farmer.setFarmerLanguage(lang);
		farmerRepo.save(farmer);
		coordinatesRepo.save(coordinates);
		log.info("FarmerServise. Farmer saved to database");
	}
	
	@Override
	@Transactional
	public ResponseEntity<Void> changeLanguage(UUID id, String language) {
		
		farmerRepo.findByid(id).get().setFarmerLanguage(language);
		redisTemplate.opsForValue().set("userID-" + id, language , languageCacheValidity, TimeUnit.MILLISECONDS);
		
		return ResponseEntity.ok().build();
	}

	@Override
	public Farmer getFarmer(UUID id) {
		
		return farmerRepo.findByid(id).get();
	}
}
