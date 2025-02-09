package telran.daily_farm.farmer.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import telran.daily_farm.api.dto.FarmerDto;
import telran.daily_farm.api.dto.LoginRequestDto;



public interface IFarmer {
	
	ResponseEntity<String> registerFarmer(FarmerDto farmerDto);
	

	
	ResponseEntity<String> removeFarmer(UUID id);
	
	String loginFarmer(LoginRequestDto loginRequestDto);

	ResponseEntity<String> updateFarmer(UUID id,FarmerDto farmerDto);

}
