package telran.daily_farm.utils.servise;


import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.daily_farm.farm_set.repo.FarmSetCategoryRepository;
import telran.daily_farm.farm_set.repo.FarmSetSizeRepository;
import telran.daily_farm.farmer.repo.FarmerRepository;
import telran.daily_farm.translate_service.LibreTranslateLocalService;

@Service
@RequiredArgsConstructor
public class UtilService  implements IUtilsService{


	private final FarmerRepository farmerRepo;
	private final StringRedisTemplate redisTemplate;
	private final FarmSetSizeRepository sizeRepo;
	private final FarmSetCategoryRepository categoryRepo;
	private final LibreTranslateLocalService translateService; 
	
	 @Value("${jwt.refresh.token.validity}")
	 private long languageCacheValidity ;
	
	@Override
	public ResponseEntity<List<String>> getFarmSetCategories(UUID id) {
//		GetFarmSetCategoryResponse res = new GetFarmSetCategoryResponse(categoryRepo.findAll().stream().map(el->el.getCategory()).toList());
		
		List<String> res = categoryRepo.findAll().stream().map(el->el.getCategory()).toList();
		
		return  translateService.translateOkResponse( ResponseEntity.ok(res), getUserLanguage(id));
	}


	@Override
	public ResponseEntity<List<String>>getFarmSetSizes(UUID id) {
	List<String> res =sizeRepo.findAll().stream().map(el->el.getSize()).toList();

		return translateService.translateOkResponse( ResponseEntity.ok(res), getUserLanguage(id));
	}
	
	@Override
	public ResponseEntity<Map<String, String>> getAllLanguages(UUID id) {
		Map<String, String> res =  translateService.getAllLanguages();
		
		return translateService.translateOkResponse( ResponseEntity.ok(res), getUserLanguage(id));
	}





	@Override
	public String getUserLanguage(UUID id) {
		String cachedLanguge =redisTemplate.opsForValue().get("userID-" + id);
		
		return cachedLanguge ==null? farmerRepo.findByid(id).get().getFarmerLanguage():cachedLanguge;
	}
}
