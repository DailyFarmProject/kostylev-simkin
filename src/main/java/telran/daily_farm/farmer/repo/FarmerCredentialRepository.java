package telran.daily_farm.farmer.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.farmer.entity.Farmer;
import telran.daily_farm.security.entity.FarmerCredential;

public interface FarmerCredentialRepository extends JpaRepository<FarmerCredential, UUID> {

	FarmerCredential findByFarmer(Farmer farmer);

	Optional<FarmerCredential> findByEmail(String email);

	boolean existsByEmail(String email);

}
