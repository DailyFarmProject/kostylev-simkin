package telran.daily_farm.security.farmer_auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import telran.daily_farm.farmer.entity.Farmer;
import telran.daily_farm.farmer.entity.FarmerCredential;
import telran.daily_farm.farmer.repo.FarmerCredentialRepository;
import telran.daily_farm.farmer.repo.FarmerRepository;
import telran.daily_farm.security.UserDetailsWithId;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FarmerDetailsService implements UserDetailsService {
	private final FarmerRepository farmerRepo;
	private final FarmerCredentialRepository farmerCredentialRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		Optional<Farmer> farmerOptional = farmerRepo.findByEmail(username);
		if (farmerOptional.isPresent()) {
			Farmer farmer = farmerOptional.get();
			FarmerCredential credential = farmerCredentialRepo.findByFarmer(farmer);
			return new UserDetailsWithId(farmer.getEmail(), credential.getHashedPassword(),
					List.of(new SimpleGrantedAuthority("ROLE_FARMER")), farmer.getId());
		}

		throw new UsernameNotFoundException(USER_NOT_FOUND);
	}
}