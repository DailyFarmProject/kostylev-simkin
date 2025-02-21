package telran.daily_farm.client.controller;

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
import telran.daily_farm.client.service.IClient;
import telran.daily_farm.security.AuthService;
import telran.daily_farm.security.JwtService;
import telran.daily_farm.security.UserDetailsWithId;

import static telran.daily_farm.api.ApiConstants.*;

@Tag(name = "Client API", description = "Methods for clients")
@RestController
@Slf4j
public class ClientController {

    @Autowired
    IClient clientService;
    @Autowired
    JwtService jwtService;
    @Autowired
    private AuthService authService;

    @Operation(summary = "Registration of new Client", description = "Register a new client")
    @PostMapping(CLIENT_REGISTER)
    public ResponseEntity<String> registerClient(@Valid @RequestBody ClientRegistrationDto clientDto) {
        log.info("Registering new client: " + clientDto.getEmail());
        return clientService.registerClient(clientDto);
    }

    @Operation(summary = "Login of Client", description = "Client login, returns accessToken and refreshToken")
    @PostMapping(CLIENT_LOGIN)
    public ResponseEntity<TokensResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return clientService.loginClient(loginRequestDto);
    }

    @Operation(summary = "Refresh token", description = "Refresh access token using refreshToken")
    @GetMapping(CLIENT_REFRESH_TOKEN)
    public ResponseEntity<RefreshTokenResponseDto> refresh(@RequestBody RefreshTokenRequestDto request) {
        log.info("Refreshing client token");
        return authService.refreshAccessToken(request.getRefreshToken());
    }

    @Operation(summary = "Update client data", description = "Update client details")
    @PutMapping(CLIENT_EDIT)
    @PreAuthorize("hasRole(ROLE_CLIENT)")
    public ResponseEntity<TokensResponseDto> updateClient(@Valid @RequestBody ClientUpdateDataRequestDto clientDto,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return clientService.updateClient(user.getId(), clientDto);
    }

    @Operation(summary = "Remove Client", description = "Delete client account")
    @DeleteMapping(CLIENT_REMOVE)
    @PreAuthorize("hasRole(ROLE_CLIENT)")
    public ResponseEntity<String> removeClient(@AuthenticationPrincipal UserDetailsWithId user) {
        return clientService.removeClient(user.getId());
    }

    @Operation(summary = "Update Client password", description = "Change client password, returns new tokens")
    @PutMapping(CLIENT_CHANGE_PASSWORD)
    @PreAuthorize("hasRole(ROLE_CLIENT)")
    public ResponseEntity<TokensResponseDto> clientUpdatePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordDto,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return clientService.updatePassword(user.getId(), changePasswordDto);
    }

    @Operation(summary = "Update Client email", description = "Change client email, returns new tokens")
    @PutMapping(CLIENT_CHANGE_EMAIL)
    @PreAuthorize("hasRole(ROLE_CLIENT)")
    public ResponseEntity<TokensResponseDto> clientUpdateEmail(@Valid @RequestBody String newEmail,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return clientService.updateEmail(user.getId(), newEmail);
    }

    @Operation(summary = "Update Client phone number", description = "Change client phone number")
    @PutMapping(CLIENT_CHANGE_PHONE)
    @PreAuthorize("hasRole(ROLE_CLIENT)")
    public ResponseEntity<String> clientUpdatePhone(@Valid @RequestBody String newPhone,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return clientService.updatePhone(user.getId(), newPhone);
    }

    @Operation(summary = "Update Client first and last name", description = "Change client name")
    @PutMapping(CLIENT_CHANGE_FIRST_LAST_NAME)
    @PreAuthorize("hasRole(ROLE_CLIENT)")
    public ResponseEntity<String> clientUpdateName(@Valid @RequestBody FullNameDto fullname,
            @AuthenticationPrincipal UserDetailsWithId user) {
        return clientService.changeName(user.getId(), fullname);
    }
}
