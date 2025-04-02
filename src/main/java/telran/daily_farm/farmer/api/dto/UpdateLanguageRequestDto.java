package telran.daily_farm.farmer.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateLanguageRequestDto {

	@NotBlank
	String newLanguage;
	
}
