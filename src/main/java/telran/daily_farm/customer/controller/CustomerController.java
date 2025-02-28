package telran.daily_farm.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.*;
import telran.daily_farm.customer.service.ICustomer;
import telran.daily_farm.security.AuthService;
import telran.daily_farm.security.JwtService;
import telran.daily_farm.security.UserDetailsWithId;

import static telran.daily_farm.api.ApiConstants.*;

@Tag(name = "Client API", description = "Methods for clients")
@RestController
@Slf4j
public class CustomerController {

    @Autowired
    ICustomer customerService;
    @Autowired
    JwtService jwtService;
    @Autowired
    private AuthService authService;

    @Operation(summary = "Registration of new customer", description = "Register a new customer")
    @PostMapping(CUSTOMER_REGISTER)
    public ResponseEntity<String> registerClient(@Valid @RequestBody CustomerRegistrationDto customerDto) {
        log.info("Registering new customer: " + customerDto.getEmail());
        return customerService.registerCustomer(customerDto);
    }

    @Operation(summary = "Login of customer", description = "Customer login, returns accessToken and refreshToken")
    @PostMapping(CUSTOMER_LOGIN)
    public ResponseEntity<TokensResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return customerService.loginCustomer(loginRequestDto);
    }

    @Operation(summary = "Refresh token", description = "Refresh access token using refreshToken")
    @GetMapping(CUSTOMER_REFRESH_TOKEN)
    public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestBody RefreshTokenRequestDto request) {
        log.info("Refreshing customer token");
        return authService.refreshAccessToken(request.getRefreshToken());
    }

    @Operation(summary = "Update customer data", description = "Update customer details")
    @PutMapping(CUSTOMER_EDIT)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<TokensResponseDto> updateCustomer(@Valid @RequestBody CustomerUpdateDataRequestDto customerDto,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.updateCustomer(user.getId(), customerDto);
    }

    @Operation(summary = "Remove customer", description = "Delete customer account")
    @DeleteMapping(CUSTOMER_REMOVE)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<String> removeCustomer(@AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.removeCustomer(user.getId());
    }

    @Operation(summary = "Update customer password", description = "Change customer password, returns new tokens")
    @PutMapping(CUSTOMER_CHANGE_PASSWORD)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<TokensResponseDto> customerUpdatePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordDto,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.updatePassword(user.getId(), changePasswordDto);
    }

    @Operation(summary = "Update customer email", description = "Change customer email, returns new tokens")
    @PutMapping(CUSTOMER_CHANGE_EMAIL)
    @PreAuthorize("hasRole(ROLE_CUSTOMER)")
    public ResponseEntity<TokensResponseDto> customerUpdateEmail(@Valid @RequestBody String newEmail,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return customerService.updateEmail(user.getId(), newEmail);
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
}
