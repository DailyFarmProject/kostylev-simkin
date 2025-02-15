package telran.daily_farm.farmer.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.Address;
import telran.daily_farm.entity.Farmer;

public interface AddressRepository extends JpaRepository<Address, UUID>{

	Address findByFarmer(Farmer farmer);

}
