package telran.daily_farm.farmer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.FarmerDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.farmer.repo.FarmerRepository;
import telran.daily_farm.security.AuthService;

import static telran.daily_farm.api.messages.ErrorMessages.*;


@Service
@Slf4j
public class FarmerService implements IFarmer{

	@Autowired
	FarmerRepository farmerRepo;
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired
	AuthService authService;
	
	
	
	@Override
	@Transactional
	public ResponseEntity<String> registerFarmer(FarmerDto farmerDto) {
		if(farmerRepo.existsById(farmerDto.getEmail()))
			throw new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_EXISTS);
		Farmer farmer = Farmer.of(farmerDto);
		farmer.setPassword(passwordEncoder.encode(farmerDto.getPassword()));
		farmer.setBalance(0.);
		farmerRepo.save(farmer);
		farmerRepo.flush();
		return ResponseEntity.ok("Farmer added successfully");
	}


	@Override
	@Transactional
	public ResponseEntity<String> updateFarmer(FarmerDto farmerDto) {
		Farmer farmer = farmerRepo.findById(farmerDto.getEmail())
				.orElseThrow(()->new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS));
		farmer.setAddress(farmerDto.getAddress());
		farmer.setEmail(farmerDto.getEmail());
		farmer.setFirstName(farmerDto.getFirstName());
		farmer.setLastName(farmerDto.getLastName());
		farmer.setPassword(passwordEncoder.encode(farmerDto.getPassword()));
		farmer.setPaypalDetails(farmerDto.getPaypalDetails());
		farmer.setPhone(farmerDto.getPhone());
		
		return ResponseEntity.ok("Farmer data updated");
	}
	
	@Override
	public String loginFarmer(LoginRequestDto loginRequestDto) {

		log.debug("loginRequestDto.getEmail()" + loginRequestDto.getEmail());
		 String token = authService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());
		 log.debug("token - " + token);
		return token;
	}

	@Override
	@Transactional
	public ResponseEntity<String> removeFarmer(String email) {
		if(!farmerRepo.existsById(email))
			throw new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS);
		farmerRepo.deleteById(email);
		return ResponseEntity.ok("Farmer removed");
	}
}
