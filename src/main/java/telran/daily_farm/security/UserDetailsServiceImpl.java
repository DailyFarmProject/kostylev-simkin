package telran.daily_farm.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import daily_farm.messages.ErrorMessages;
import telran.daily_farm.client.repo.ClientRepository;
import telran.daily_farm.entity.Client;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.farmer.repo.FarmerRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    private final ClientRepository clientRepo;
    private final FarmerRepository farmerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Client> clientOptional = clientRepo.findById(username);
        if (clientOptional.isPresent()) {
            Client client = clientOptional.get();
            return new User(client.getEmail(), client.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_CLIENT")));
        }

        Optional<Farmer> farmerOptional = farmerRepo.findById(username);
        if (farmerOptional.isPresent()) {
        	Farmer farmer = farmerOptional.get();
        	User user = new User(farmer.getEmail(), farmer.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_FARMER")));
        	log.debug("farmer exists role - " + user.getAuthorities());
            //return new User(farmer.getEmail(), farmer.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_FARMER")));\
        	return user;
        }

        throw new UsernameNotFoundException(ErrorMessages.USER_NOT_FOUND);
    }
}