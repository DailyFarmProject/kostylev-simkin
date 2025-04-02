package telran.daily_farm.farm_set.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.farm_set.entity.FarmSetCategory;

public interface FarmSetCategoryRepository extends JpaRepository<FarmSetCategory, UUID>{

	Optional<FarmSetCategory> findByCategory(String category);

}
