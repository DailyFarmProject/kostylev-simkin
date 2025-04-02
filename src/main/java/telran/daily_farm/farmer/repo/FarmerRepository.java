package telran.daily_farm.farmer.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.farmer.entity.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, UUID>{

	

	

	Optional<Farmer> findByid(UUID id);



}
