package telran.daily_farm.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.customer.api.dto.*;

import telran.daily_farm.customer.service.ICustomer;
import telran.daily_farm.security.UserDetailsWithId;
import telran.daily_farm.security.api.dto.RefreshTokenRequestDto;
import telran.daily_farm.security.api.dto.RefreshTokenResponseDto;
import telran.daily_farm.security.api.dto.TokensResponseDto;
import telran.daily_farm.security.customer_auth.CustomerAuthService;
import telran.daily_farm.security.token.JwtService;

import static telran.daily_farm.api.ApiConstants.*;

@RestController
@Slf4j
public class CustomerController {

    @Autowired
    ICustomer customerService;
    @Autowired
    JwtService jwtService;
    @Autowired
    private CustomerAuthService authService;
    
  //Registration and verification

    @PostMapping(CUSTOMER_REGISTER)
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerRegistrationDto customerDto) {
        log.info("Registering new customer: " + customerDto.getEmail());
        return customerService.registerCustomer(customerDto);
    }
    
    @GetMapping(CUSTOMER_EMAIL_VERIFICATION)
    public ResponseEntity<String> emailVerification(@RequestParam("token") String token) {
        log.info("Received request to verify email. Token: {}", token);

        if (token == null || token.isEmpty()) {
            log.error("Error: there is no token!");
            return ResponseEntity.badRequest().body("Error: there is no token!");
        }
        return customerService.emailVerification(token);
    }
    
    @GetMapping(CUSTOMER_EMAIL_VERIFICATION_RESEND)
    public ResponseEntity<String> resendVerificationLink(@Valid @RequestBody SendToRequestDto sendToRequestDto) {
        return customerService.resendVerificationLink(sendToRequestDto.getEmail());
    }
    
    //Authorization and logout

    @PostMapping(CUSTOMER_LOGIN)
    public ResponseEntity<TokensResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return customerService.loginCustomer(loginRequestDto);
    }
    
    @DeleteMapping(CUSTOMER_LOGOUT)
    public ResponseEntity<String> logoutCustomer(@AuthenticationPrincipal UserDetailsWithId user,
                                                  @RequestHeader("Authorization") String token) {
        return customerService.logoutCustomer(user.getId(), token);
    }
    
  //Changing and restoring the password
    
    @PutMapping(CUSTOMER_CHANGE_PASSWORD)
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<TokensResponseDto> customerUpdatePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordDto,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.updatePassword(user.getId(), changePasswordDto);
    }
    
    @GetMapping(CUSTOMER_RESET_PASSWORD)
    public ResponseEntity<String> generateAndSendNewPassword(@Valid @RequestBody SendToRequestDto sendToRequestDto) {
        return customerService.generateAndSendNewPassword(sendToRequestDto.getEmail());
    }
    
    //Changing customer's data
    
    @PutMapping(CUSTOMER_EDIT)
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<String> updateCustomer(@Valid @RequestBody CustomerUpdateDataRequestDto customerDto,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.updateCustomer(user.getId(), customerDto);
    }
   
    @PutMapping(CUSTOMER_CHANGE_PHONE)
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<String> customerUpdatePhone(@Valid @RequestBody String newPhone,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.updatePhone(user.getId(), newPhone);
    }

    @PutMapping(CUSTOMER_CHANGE_FIRST_LAST_NAME)
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<String> clientUpdateName(@Valid @RequestBody FullNameDto fullname,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.changeName(user.getId(), fullname);
    }
    
    //Deleting an account
    
    @DeleteMapping(CUSTOMER_REMOVE)
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<String> removeCustomer(@AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.removeCustomer(user.getId());
    }
    
  //Email Update


    @PutMapping(CUSTOMER_CHANGE_EMAIL)
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public ResponseEntity<String> customerUpdateEmail(@RequestParam String token) {
        return customerService.updateEmail(token);
    }
    
 @PreAuthorize("hasRole('ROLE_CUSTOMER')")
 @GetMapping(CUSTOMER_EMAIL_CHANGE_VERIFICATION)
 public ResponseEntity<String> sendVerificationTokenForUpdateEmail(
         @RequestParam String newEmail,
         @AuthenticationPrincipal UserDetailsWithId user) {
     return customerService.sendVerificationTokenForUpdateEmail(user.getId(), newEmail);
 }

 @GetMapping(CUSTOMER_NEW_EMAIL_VERIFICATION)
 public ResponseEntity<String> sendVerificationTokenToNewEmail(@RequestParam String token) {
     return customerService.sendVerificationTokenToNewEmail(token);
 }

// Other
	@PostMapping(CUSTOMER_REFRESH_TOKEN)
	public ResponseEntity<RefreshTokenResponseDto> refresh(
	        @RequestBody RefreshTokenRequestDto request) {
	    log.info("Controller: Refresh token starts");
	    return authService.refreshAccessTokenCustomer(request.getRefreshToken());
	}
}
