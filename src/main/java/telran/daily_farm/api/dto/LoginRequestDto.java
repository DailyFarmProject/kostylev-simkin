package telran.daily_farm.api.dto;

import static daily_farm.messages.ErrorMessages.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoginRequestDto {
	
	@NotBlank( message = EMAIL_IS_NOT_VALID)
	@Email( message = EMAIL_IS_NOT_VALID)
	String email;
	
	@Size(min = 8, message = PASSWORD_IS_NOT_VALID)
    private String password;

}
