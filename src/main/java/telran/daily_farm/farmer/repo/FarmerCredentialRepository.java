package telran.daily_farm.farmer.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.farmer.entity.Farmer;
import telran.daily_farm.farmer.entity.FarmerCredential;

public interface FarmerCredentialRepository extends JpaRepository<FarmerCredential, UUID> {

	FarmerCredential findByFarmer(Farmer farmer);

}
