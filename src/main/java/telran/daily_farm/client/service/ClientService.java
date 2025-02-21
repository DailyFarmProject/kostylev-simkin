package telran.daily_farm.client.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.*;
import telran.daily_farm.client.repo.ClientCredentialRepository;
import telran.daily_farm.client.repo.ClientRepository;
import telran.daily_farm.entity.Client;
import telran.daily_farm.entity.ClientCredential;
import telran.daily_farm.security.AuthService;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientService implements IClient {

    private final ClientRepository clientRepo;
    private final ClientCredentialRepository credentialRepo;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    @Override
    @Transactional
    public ResponseEntity<String> registerClient(ClientRegistrationDto clientDto) {
        log.info("Service: Registering client - " + clientDto.getEmail());

        checkEmailIsUnique(clientDto.getEmail());
        Client client = Client.of(clientDto);
        clientRepo.save(client);

        ClientCredential credential = ClientCredential.builder()
                .createdAt(LocalDateTime.now())
                .passwordLastUpdated(LocalDateTime.now())
                .hashedPassword(passwordEncoder.encode(clientDto.getPassword()))
                .client(client)
                .build();
        credentialRepo.save(credential);

        return ResponseEntity.ok("Client registered successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<TokensResponseDto> loginClient(LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(authService.authenticate(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
    }

    @Override
    @Transactional
    public ResponseEntity<String> removeClient(UUID id) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        clientRepo.delete(client);
        return ResponseEntity.ok("Client removed successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<TokensResponseDto> updatePassword(UUID id, ChangePasswordRequest changePasswordDto) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
        ClientCredential credential = credentialRepo.findByClient(client);

        if (!passwordEncoder.matches(changePasswordDto.getOldPassword(), credential.getHashedPassword()))
            throw new IllegalArgumentException("Invalid old password");

        credential.setHashedPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        credential.setPasswordLastUpdated(LocalDateTime.now());

        return ResponseEntity.ok(authService.authenticate(client.getEmail(), credentialRepo.findByClient(client).getHashedPassword()));
    }

    @Override
    @Transactional
    public ResponseEntity<TokensResponseDto> updateEmail(UUID id, String newEmail) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
    
        checkEmailIsUnique(newEmail);
        client.setEmail(newEmail);

        return ResponseEntity.ok(authService.authenticate(client.getEmail(), credentialRepo.findByClient(client).getHashedPassword()));
    }

    @Override
    @Transactional
    public ResponseEntity<String> updatePhone(UUID id, String newPhone) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
    
        client.setPhone(newPhone);
        return ResponseEntity.ok("Phone updated successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<String> changeName(UUID id, FullNameDto fullname) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));
    
        client.setFirstName(fullname.getFirstName());
        client.setLastName(fullname.getLastName());
        return ResponseEntity.ok("First name and last name updated successfully");
    }

    @Override
    @Transactional
    public ResponseEntity<TokensResponseDto> updateClient(UUID id, ClientUpdateDataRequestDto clientDto) {
        Client client = clientRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client not found"));

        String newEmail = clientDto.getEmail();
        TokensResponseDto tokens = client.getEmail().equals(newEmail) ? 
                new TokensResponseDto() : authService.authenticate(newEmail, credentialRepo.findByClient(client).getHashedPassword());

        client.setFirstName(clientDto.getFirstName());
        client.setLastName(clientDto.getLastName());
        client.setPhone(clientDto.getPhone());

        return ResponseEntity.ok(tokens);
    }

    private void checkEmailIsUnique(String email) {
        if (clientRepo.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Client with this email already exists");
    }
}
