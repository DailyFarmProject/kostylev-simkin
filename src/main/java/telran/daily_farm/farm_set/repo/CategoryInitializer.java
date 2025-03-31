package telran.daily_farm.farm_set.repo;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import telran.daily_farm.farm_set.entity.FarmSet;
import telran.daily_farm.farm_set.entity.FarmSetCategory;


@Component
public class CategoryInitializer {

	@Value("${farmset.categories.list}")
	private List<String> LIST_OF_FARM_SET_CATEGORIES;
	
	FarmSetCategoryRepository categoryRepo;
	public CategoryInitializer(FarmSetCategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
      
    }
	

	@PostConstruct
	public void init() {
		if (categoryRepo.count() == 0) {
			List<FarmSetCategory> initList = LIST_OF_FARM_SET_CATEGORIES.stream().map(cat -> FarmSetCategory.builder()
					.category(cat).farmSets(new HashSet<FarmSet>()).build()).toList();
			categoryRepo.saveAll(initList);
		}
	}
}
