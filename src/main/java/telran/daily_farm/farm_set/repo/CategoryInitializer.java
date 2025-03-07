package telran.daily_farm.farm_set.repo;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import telran.daily_farm.entity.farm_set.FarmSet;
import telran.daily_farm.entity.farm_set.FarmSetCategory;

import static telran.daily_farm.api.ApiConstants.*;

@Component
public class CategoryInitializer {

	FarmSetCategoryRepository categoryRepo;
	public CategoryInitializer(FarmSetCategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
      
    }
	

	@PostConstruct
	public void init() {
		if (categoryRepo.count() == 0) {
			List<FarmSetCategory> initList = LIST_OF_CATEGORIES.stream().map(cat -> FarmSetCategory.builder()
					.category(cat).farmSets(new HashSet<FarmSet>()).build()).toList();
			categoryRepo.saveAll(initList);
		}
	}
}
