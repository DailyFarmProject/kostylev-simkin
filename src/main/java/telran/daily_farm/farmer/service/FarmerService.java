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

import java.util.UUID;

@Service
@Slf4j
public class FarmerService implements IFarmer {

	@Autowired
	FarmerRepository farmerRepo;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	AuthService authService;

	@Override
	@Transactional
	public ResponseEntity<String> registerFarmer(FarmerDto farmerDto) {
		
		checkEmailIsUnique(farmerDto.getEmail());
		
		Farmer farmer = Farmer.of(farmerDto);
		farmer.setPassword(passwordEncoder.encode(farmerDto.getPassword()));
		farmer.setBalance(0.);
		farmerRepo.save(farmer);
		farmerRepo.flush();
		return ResponseEntity.ok("Farmer added successfully");
	}

	@Override
	@Transactional
	public ResponseEntity<String> updateFarmer(UUID id, FarmerDto farmerDto) {
		Farmer farmer = farmerRepo.findByid(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS));
		String emailOld = farmer.getEmail();
		String emailNew = farmerDto.getEmail();
		if (!emailOld.equals(emailNew))
			checkEmailIsUnique(emailNew);

		farmer.setAddress(farmerDto.getAddress());
		farmer.setEmail(farmerDto.getEmail());
		farmer.setFirstName(farmerDto.getFirstName());
		farmer.setLastName(farmerDto.getLastName());
		farmer.setPassword(passwordEncoder.encode(farmerDto.getPassword()));
		farmer.setPaypalDetails(farmerDto.getPaypalDetails());
		farmer.setPhone(farmerDto.getPhone());

		return ResponseEntity.ok("Farmer data updated");
	}

	private void checkEmailIsUnique(String email) {
		if (farmerRepo.existsByEmail(email))
			throw new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_EXISTS);
	}

	@Override
	public String loginFarmer(LoginRequestDto loginRequestDto) {
		String token = authService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());
		return token;
	}

	@Override
	@Transactional
	public ResponseEntity<String> removeFarmer(UUID id) {
		if(!farmerRepo.existsById(id))
			throw new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS);
		farmerRepo.deleteById(id);
		return ResponseEntity.ok("Farmer removed");
	}

}
