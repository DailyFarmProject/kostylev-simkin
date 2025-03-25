package telran.daily_farm.farmer.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.PHONE_NUMBER_IS_NOT_VALID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UpdatePhoneRequestDto {

	@Schema(description = "phone", example = "22155665")
	@Pattern(regexp = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}", message = PHONE_NUMBER_IS_NOT_VALID)
	private String phone;
}
