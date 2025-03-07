package telran.daily_farm.api.dto.farm_set;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetAllLanguagesResponse {
	
	Map<String, String> languages;
}
