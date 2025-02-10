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
import telran.daily_farm.location.service.ILocationService;
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
	
	@Autowired
	ILocationService locationService;

	@Override
	@Transactional
	public ResponseEntity<String> registerFarmer(FarmerDto farmerDto) {
		log.info("Servise. Registration of new farmer - " + farmerDto.getEmail());
		
		checkEmailIsUnique(farmerDto.getEmail());
		log.info("Servise. Checked. Email is unique");

		Farmer farmer = Farmer.of(farmerDto);
		log.debug("Servise. Created Entity farmer from dto");
		
		if(farmerDto.getCoordinates() == null) {
			farmer.setCoordinates(locationService.getCoordinatesFromAddress(farmerDto.getAddress()));
			log.info("Servise. Dto does'n contain coordinates. Coordinates added successfully from address(LocationService)");
		}
		
		if(farmerDto.getAddress() == null) {
			farmer.setAddress(locationService.getAddtessFromCoordinates(farmerDto.getCoordinates()));
			log.info("Servise. Dto does'n contain addres. Address added successfully from coordinates (LocationService)");
		}
		
		farmer.setPassword(passwordEncoder.encode(farmerDto.getPassword()));
		log.info("Servise. Password hash added successfully");
		
		farmer.setBalance(0.);
		log.info("Servise. Starting balanse - 0 added successfully");
		
		farmerRepo.save(farmer);
		log.info("Servise. Farmer saved to database");
		
		farmerRepo.flush();
		return ResponseEntity.ok("Farmer added successfully");
	}

	@Override
	@Transactional
	public ResponseEntity<String> updateFarmer(UUID id, FarmerDto farmerDto) {
		log.info("update farmer data starts");
		Farmer farmer = farmerRepo.findByid(id).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.CONFLICT, FARMER_WITH_THIS_EMAIL_IS_NOT_EXISTS));
		
		String emailOld = farmer.getEmail();
		String emailNew = farmerDto.getEmail();
		if (!emailOld.equals(emailNew)) {
			checkEmailIsUnique(emailNew);
			log.info("checked new email is unique + update - successfully");
		}
		farmer.setEmail(farmerDto.getEmail());
		log.info("email is update - successfully");
		
		if(farmerDto.getCoordinates() == null) 
			farmer.setCoordinates(locationService.getCoordinatesFromAddress(farmerDto.getAddress()));
		else 
			farmer.setCoordinates(farmerDto.getCoordinates());
		log.info("coordinates updated successfully");
		
		if(farmerDto.getAddress() == null)
			farmer.setAddress(locationService.getAddtessFromCoordinates(farmerDto.getCoordinates()));
		else
			farmer.setAddress(farmerDto.getAddress());
		log.info("address updated successfully");
		
		
		
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
		log.debug("Service. Request to login farmer -" + loginRequestDto.getEmail());
		String token = authService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword());
		log.debug("Service. Login successfull, token returned to user");
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
