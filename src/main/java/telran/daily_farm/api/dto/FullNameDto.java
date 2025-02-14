package telran.daily_farm.api.dto;

import static daily_farm.messages.ErrorMessages.LAST_NAME_IS_NOT_VALID;
import static daily_farm.messages.ErrorMessages.NAME_IS_NOT_VALID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class FullNameDto {

	@NotBlank
	@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = NAME_IS_NOT_VALID)
	String firstName;
	
	@NotBlank
	@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = LAST_NAME_IS_NOT_VALID)
	String lastName;
}
