package telran.daily_farm.api.dto;

import static daily_farm.messages.ErrorMessages.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
public abstract  class BaseUser {
	
	@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = NAME_IS_NOT_VALID)
	String firstName;
	
	@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = LAST_NAME_IS_NOT_VALID)
	String lastName;
	
	@Pattern(regexp = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}", message = PHONE_NUMBER_IS_NOT_VALID)
	String phone;
	
	@NotBlank( message = EMAIL_IS_NOT_VALID)
	@Email( message = EMAIL_IS_NOT_VALID)
	String email;
	
	@Size(min = 8, message = PASSWORD_IS_NOT_VALID)
    private String password;

}
