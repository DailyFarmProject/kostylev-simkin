package telran.daily_farm.farmer.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.Coordinates;
import telran.daily_farm.entity.Farmer;

public interface CoordinatesRepository extends JpaRepository<Coordinates, UUID>{

	Coordinates findByFarmer(Farmer farmer);

}
