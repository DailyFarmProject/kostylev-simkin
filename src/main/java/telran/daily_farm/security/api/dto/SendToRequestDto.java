package telran.daily_farm.security.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.EMAIL_IS_NOT_VALID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SendToRequestDto {

	@NotBlank( message = EMAIL_IS_NOT_VALID)
	@Email( message = EMAIL_IS_NOT_VALID)
	private String email;
}
