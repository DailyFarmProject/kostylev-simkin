package telran.daily_farm.farm_set.repo;

import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import telran.daily_farm.entity.farm_set.FarmSet;
import telran.daily_farm.entity.farm_set.FarmSetSize;

import static telran.daily_farm.api.ApiConstants.*;

@Component
public class SizeInitializer {

	FarmSetSizeRepository sizeRepo;
	public SizeInitializer(FarmSetSizeRepository sizeRepo) {
        this.sizeRepo = sizeRepo;
      
    }
	

	@PostConstruct
	public void init() {
		if (sizeRepo.count() == 0) {
			List<FarmSetSize> initList = SIZE_LIST.stream().map(size -> FarmSetSize.builder()
					.size(size)
					.farmSets(new HashSet<FarmSet>())
					.build()).toList();
			sizeRepo.saveAll(initList);
		}
	}
}
