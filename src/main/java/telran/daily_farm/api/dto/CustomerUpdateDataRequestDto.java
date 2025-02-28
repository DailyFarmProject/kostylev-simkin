package telran.daily_farm.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CustomerUpdateDataRequestDto {
    
    @Schema(description = "firstName", example = "John")
    @Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = NAME_IS_NOT_VALID)
    private String firstName;

    @Schema(description = "lastName", example = "Doe")
    @Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = LAST_NAME_IS_NOT_VALID)
    private String lastName;

    @Schema(description = "phone", example = "999888585")
    @Pattern(regexp = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}", message = PHONE_NUMBER_IS_NOT_VALID)
    private String phone;

    @Schema(description = "email", example = "john.doe@mail.com")
    @Email(message = EMAIL_IS_NOT_VALID)
    private String email;

    @Schema(description = "Customer's city (optional)", example = "Tel Aviv")
    private String city;
}
