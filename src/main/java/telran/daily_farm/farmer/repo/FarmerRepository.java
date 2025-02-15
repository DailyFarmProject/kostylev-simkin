package telran.daily_farm.farmer.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.Farmer;
import telran.daily_farm.entity.FarmerCredential;

public interface FarmerRepository extends JpaRepository<Farmer, UUID>{

	boolean existsByEmail(String email);

	Optional<Farmer> findByEmail(String username);

	Optional<Farmer> findByid(UUID id);



}
