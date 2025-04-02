package telran.daily_farm.farmer.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import telran.daily_farm.farmer.api.dto.CoordinatesDto;
import telran.daily_farm.farmer.api.dto.FarmerUpdateDataRequestDto;
import telran.daily_farm.farmer.entity.Farmer;

public interface IFarmer {

	void createFarmer(Farmer farmer, CoordinatesDto coordinatesDto, String lang);


	ResponseEntity<String> updateCoordinates(UUID uuid, CoordinatesDto coordinatesDto);

	ResponseEntity<String> updatePhone(UUID uuid, String newPhone);

	ResponseEntity<String> updateFarmer(UUID id, FarmerUpdateDataRequestDto farmerDto);

	ResponseEntity<String> updateCompany(UUID id, String company);
	
	ResponseEntity<Void> changeLanguage(UUID id, String language);

}
