package telran.daily_farm.farmer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.FarmerDto;
import telran.daily_farm.api.dto.LoginRequestDto;
import telran.daily_farm.farmer.service.IFarmer;

import static telran.daily_farm.api.ApiConstants.*;
import static telran.daily_farm.api.messages.ErrorMessages.*;


@RestController
@Slf4j
public class FarmerController{
	
	@Autowired
	IFarmer farmerService;
	

	@PostMapping(FARMER_REGISTER)
	public ResponseEntity<String> registerFarmer(@Valid @RequestBody FarmerDto farmerDto) {
		return farmerService.registerFarmer(farmerDto);
	}
	
	@PostMapping(FARMER_LOGIN)
	public String login(@RequestBody LoginRequestDto loginRequestDto) {
	     return  farmerService.loginFarmer(loginRequestDto);
	}

	@PutMapping(FARMER_EDIT)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> updateFarmer(@Valid @RequestBody FarmerDto farmerDto) {
		 String emailFromContext = SecurityContextHolder.getContext().getAuthentication().getName();

		    if (!emailFromContext.equals(farmerDto.getEmail())) {
		        throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_EDIT_OWN_ACCOUNT_ONLY);
		    }

		    return farmerService.updateFarmer(farmerDto);
	}
	
	@DeleteMapping(FARMER_REMOVE + "{email}")
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<String> removeFarmer(@PathVariable String email) {
		String emailFromContext = SecurityContextHolder.getContext().getAuthentication().getName();
		log.debug("emailFromContext - " + emailFromContext);
	    if (!emailFromContext.equals(email)) {
	        throw new ResponseStatusException(HttpStatus.FORBIDDEN, ERROR_DELETE_OWN_ACCOUNT_ONLY);
	    }

	    return farmerService.removeFarmer(email);
	}
	


}
