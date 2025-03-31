package telran.daily_farm.utils.servise;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;

public interface IUtilsService {

	
	ResponseEntity<Map<String, String>> getAllLanguages(UUID id);



	ResponseEntity<List<String>> getFarmSetCategories(UUID id);

	ResponseEntity<List<String>> getFarmSetSizes(UUID id);
	
	String getUserLanguage(UUID id);
}
