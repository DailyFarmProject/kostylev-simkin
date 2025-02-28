package telran.daily_farm.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.COMPANY_NAME_IS_REQUIRED;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdateCompanyRequestDto {
	
	@Schema(description = "company name")
	@NotBlank(message = COMPANY_NAME_IS_REQUIRED)
	private String company;

}
