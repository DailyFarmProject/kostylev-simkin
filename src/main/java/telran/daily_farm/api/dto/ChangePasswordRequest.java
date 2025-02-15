package telran.daily_farm.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import io.swagger.v3.oas.annotations.media.Schema;

@NoArgsConstructor
@Getter
public class ChangePasswordRequest {


	@Schema(description = "oldPassword", example = "12345678")
	@NotBlank(message = OLD_NEW_PASSWORD_REQUARED)
	@Size(min = 8, message = PASSWORD_IS_NOT_VALID)
	String oldPassword;
	
	@Schema(description = "newPassword", example = "123456789")
	@Size(min = 8, message = PASSWORD_IS_NOT_VALID)
	@NotBlank(message = OLD_NEW_PASSWORD_REQUARED)
	String newPassword;
}
