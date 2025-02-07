package telran.daily_farm.farmer.service;

import org.springframework.http.ResponseEntity;

import telran.daily_farm.api.dto.FarmerDto;
import telran.daily_farm.api.dto.LoginRequestDto;



public interface IFarmer {
	
	ResponseEntity<String> registerFarmer(FarmerDto farmerDto);
	
	ResponseEntity<String>  updateFarmer(FarmerDto farmerDto);
	
	ResponseEntity<String> removeFarmer(String email);
	
	String loginFarmer(LoginRequestDto loginRequestDto);

}
