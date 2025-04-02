package telran.daily_farm.farmer.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.EMAIL_IS_NOT_VALID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SendToRequestDto {

	@Schema(description = "email", example = "bob@bobmail.bob")
	@NotBlank( message = EMAIL_IS_NOT_VALID)
	@Email( message = EMAIL_IS_NOT_VALID)
	private String email;
}
