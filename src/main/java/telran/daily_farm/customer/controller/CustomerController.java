package telran.daily_farm.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.*;
import telran.daily_farm.customer.service.ICustomer;
import telran.daily_farm.security.AuthService;
import telran.daily_farm.security.JwtService;
import telran.daily_farm.security.UserDetailsWithId;

import static telran.daily_farm.api.ApiConstants.*;

@Tag(name = "Customer API", description = "Methods for customers")
@RestController
@Slf4j
public class CustomerController {

    @Autowired
    ICustomer customerService;
    @Autowired
    JwtService jwtService;
    @Autowired
    private AuthService authService;
    
  //Registration and verification

    @Operation(summary = "Registration of new customer", description = "Register a new customer")
    @PostMapping(CUSTOMER_REGISTER)
    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerRegistrationDto customerDto) {
        log.info("Registering new customer: " + customerDto.getEmail());
        return customerService.registerCustomer(customerDto);
    }
    
    @Operation(summary = "Email verification", description = "Verify customer email with a token")
    @GetMapping(CUSTOMER_EMAIL_VERIFICATION)
    public ResponseEntity<String> emailVerification(@RequestParam("token") String token) {
        log.info("Received request to verify email. Token: {}", token);

        if (token == null || token.isEmpty()) {
            log.error("Error: there is no token!");
            return ResponseEntity.badRequest().body("Error: there is no token!");
        }
        return customerService.emailVerification(token);
    }
    
    @Operation(summary = "Resend verification link", description = "Resend email verification link")
    @GetMapping(CUSTOMER_EMAIL_VERIFICATION_RESEND)
    public ResponseEntity<String> resendVerificationLink(@Valid @RequestBody SendToRequestDto sendToRequestDto) {
        return customerService.resendVerificationLink(sendToRequestDto.getEmail());
    }
    
    //Authorization and logout

    @Operation(summary = "Login of customer", description = "Customer login, returns accessToken and refreshToken")
    @PostMapping(CUSTOMER_LOGIN)
    public ResponseEntity<TokensResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return customerService.loginCustomer(loginRequestDto);
    }
    
    @Operation(summary = "Logout customer", description = "Logout and blacklist access token")
    @DeleteMapping(CUSTOMER_LOGOUT)
    public ResponseEntity<String> logoutCustomer(@AuthenticationPrincipal UserDetailsWithId user,
                                                  @RequestHeader("Authorization") String token) {
        return customerService.logoutCustomer(user.getId(), token);
    }
    
  //Changing and restoring the password
    
    @Operation(summary = "Update customer password", description = "Change customer password, returns new tokens")
    @PutMapping(CUSTOMER_CHANGE_PASSWORD)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<TokensResponseDto> customerUpdatePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordDto,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.updatePassword(user.getId(), changePasswordDto);
    }
    
    @Operation(summary = "Reset password", description = "Generate and send a new password to the customer's email")
    @GetMapping(CUSTOMER_RESET_PASSWORD)
    public ResponseEntity<String> generateAndSendNewPassword(@Valid @RequestBody SendToRequestDto sendToRequestDto) {
        return customerService.generateAndSendNewPassword(sendToRequestDto.getEmail());
    }
    
    //Changing customer's data
    
    @Operation(summary = "Update customer data", description = "Update customer details")
    @PutMapping(CUSTOMER_EDIT)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<String> updateCustomer(@Valid @RequestBody CustomerUpdateDataRequestDto customerDto,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.updateCustomer(user.getId(), customerDto);
    }
   
    @Operation(summary = "Update customer phone number", description = "Change customer phone number")
    @PutMapping(CUSTOMER_CHANGE_PHONE)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<String> customerUpdatePhone(@Valid @RequestBody String newPhone,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.updatePhone(user.getId(), newPhone);
    }

    @Operation(summary = "Update customer first and last name", description = "Change customer name")
    @PutMapping(CUSTOMER_CHANGE_FIRST_LAST_NAME)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<String> clientUpdateName(@Valid @RequestBody FullNameDto fullname,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.changeName(user.getId(), fullname);
    }
    
    //Deleting an account
    
    @Operation(summary = "Remove customer", description = "Delete customer account")
    @DeleteMapping(CUSTOMER_REMOVE)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<String> removeCustomer(@AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.removeCustomer(user.getId());
    }
    
  //Email Update


    @Operation(summary = "Update customer email", description = "Change customer email, requires email verification")
    @PutMapping(CUSTOMER_CHANGE_EMAIL)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<String> customerUpdateEmail(@RequestParam String token) {
        return customerService.updateEmail(token);
    }
    
    @Operation(summary = "Send verification token for updating email", 
            description = "Sends a verification token to confirm updating the customer's email address")
 @PreAuthorize("hasRole(ROLE_CUSTOMER)")
 @GetMapping(CUSTOMER_EMAIL_CHANGE_VERIFICATION)
 public ResponseEntity<String> sendVerificationTokenForUpdateEmail(
         @RequestParam String newEmail,
         @AuthenticationPrincipal UserDetailsWithId user) {
     return customerService.sendVerificationTokenForUpdateEmail(user.getId(), newEmail);
 }

 @Operation(summary = "Send verification token to new email", 
            description = "Sends a verification token to the new email address after initial verification")
 @GetMapping(CUSTOMER_NEW_EMAIL_VERIFICATION)
 public ResponseEntity<String> sendVerificationTokenToNewEmail(@RequestParam String token) {
     return customerService.sendVerificationTokenToNewEmail(token);
 }

// Other
 @Operation(summary = "Refresh token",
	        description = "When accessToken expires, any request throws an exception. You need to refresh the token by sending refreshToken to this endpoint. Returns new accessToken")
	@PostMapping(CUSTOMER_REFRESH_TOKEN)
	public ResponseEntity<RefreshTokenResponseDto> refresh(
	        @Parameter(description = "JWT refresh token", required = true) @RequestBody RefreshTokenRequestDto request) {
	    log.info("Controller: Refresh token starts");
	    return authService.refreshAccessTokenCustomer(request.getRefreshToken());
	}
}
