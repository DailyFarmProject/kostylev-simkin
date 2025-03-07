package telran.daily_farm.farm_set.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.farm_set.FarmSetSize;

public interface FarmSetSizeRepository  extends JpaRepository<FarmSetSize, UUID>{

	Optional<FarmSetSize> findBySize(String size);

}
