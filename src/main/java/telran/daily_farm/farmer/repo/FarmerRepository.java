package telran.daily_farm.farmer.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.Farmer;

public interface FarmerRepository extends JpaRepository<Farmer, String>{

}
