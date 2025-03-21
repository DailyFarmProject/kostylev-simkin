package telran.daily_farm.farm_set.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.farm_set.FarmSet;

public interface FarmSetRepository extends JpaRepository<FarmSet, UUID>{

}
