package telran.daily_farm.farm_set.api.dto;

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
