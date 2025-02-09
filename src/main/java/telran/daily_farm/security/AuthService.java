package telran.daily_farm.security;



import  static daily_farm.messages.ErrorMessages.*;
import lombok.RequiredArgsConstructor;
import telran.daily_farm.client.repo.ClientRepository;
import telran.daily_farm.entity.Client;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.farmer.repo.FarmerRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final FarmerRepository farmerRepo;
    private final ClientRepository clientRepo;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public String authenticate(String email, String password) {
        Optional<Farmer> farmerOptional = farmerRepo.findByEmail(email);
        Optional<Client> clientOptional = clientRepo.findByEmail(email);

//        if (clientOptional.isPresent()) {
//        	Client client = clientOptional.get();
//            if (passwordEncoder.matches(password, client.getPassword())) {
//                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
//                String uuid = client.getId().toString();
//                return jwtService.generateToken(uuid, email);
//            }
//        } else
        if (farmerOptional.isPresent()) {
        	Farmer farmer = farmerOptional.get();
            if (passwordEncoder.matches(password, farmer.getPassword())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
                String uuid = farmer.getId().toString();
                return jwtService.generateToken(uuid,email);
            }
        }
        throw new RuntimeException(WRONG_USER_NAME_OR_PASSWORD);
    }
}