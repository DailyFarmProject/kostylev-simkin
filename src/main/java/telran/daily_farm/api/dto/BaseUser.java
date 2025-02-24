package telran.daily_farm.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import io.swagger.v3.oas.annotations.media.Schema;
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
	
//	@Schema(description = "firstName", example = "Bob")
//	//@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = NAME_IS_NOT_VALID)
//	String firstName;
//	
//	@Schema(description = "lastName", example = "Bobovsky")
//	//@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = LAST_NAME_IS_NOT_VALID)
//	String lastName;
	
	@Schema(description = "phone", example = "22155665")
	@Pattern(regexp = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}", message = PHONE_NUMBER_IS_NOT_VALID)
	@NotBlank( message = PHONE_IS_REQUIRED)
	String phone;
	
	@Schema(description = "email", example = "bob@bobmail.bob")
	@NotBlank( message = EMAIL_IS_NOT_VALID)
	@Email( message = EMAIL_IS_NOT_VALID)
	String email;
	
	@Schema(description = "password", example = "12345678")
	@Size(min = 8, message = PASSWORD_IS_NOT_VALID)
	@NotBlank( message = PASSWORD_IS_REQUIRED)
    private String password;

}
