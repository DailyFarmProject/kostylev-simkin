package telran.daily_farm.utils.controller;

import static telran.daily_farm.api.ApiConstants.*;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import telran.daily_farm.security.UserDetailsWithId;
import telran.daily_farm.translate_service.LibreTranslateLocalService;
import telran.daily_farm.utils.servise.IUtilsService;

@RestController
@RequiredArgsConstructor
public class UtilsController  {
	
	private final IUtilsService utilsService;
	private final LibreTranslateLocalService translateService; 
	
	
	@GetMapping(GET_LANGUAGES)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public  ResponseEntity<Map<String,String>> getAllLanguages(@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token){
		return utilsService.getAllLanguages(user.getId());
	}
	

	@GetMapping(GET_CATEGORIES)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<List<String>> getFarmSetCategories(
			@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return utilsService.getFarmSetCategories(user.getId());
	}

	@GetMapping(GET_SIZES)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<List<String>> getFarmSetSizes(@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token) {
		return utilsService.getFarmSetSizes(user.getId());
	}


	@PostMapping(CHANGE_USER_LANGUAGE)
	@PreAuthorize("hasRole(ROLE_FARMER)")
	public ResponseEntity<Void> changeLanguage(@AuthenticationPrincipal UserDetailsWithId user,
			@Parameter(description = "JWT токен", required = true) @RequestHeader("Authorization") String token,
			@RequestBody String language) {
		
		if(!translateService.getAllLanguages().keySet().contains(language))
			return ResponseEntity.badRequest().build();
		
		
		return utilsService.changeLanguage(user.getId(), language);
	}


	


}
