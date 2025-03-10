package telran.daily_farm.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.LAST_NAME_IS_NOT_VALID;
import static telran.daily_farm.api.messages.ErrorMessages.NAME_IS_NOT_VALID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class CustomerRegistrationDto extends BaseUser {

    @Schema(description = "Customer's city (optional)", example = "Tel Aviv")
    private String city;

    @Schema(description = "Customer's coordinates (optional, used for delivery)")
    private CoordinatesDto coordinates;
    
	@Schema(description = "firstName", example = "Bob", nullable = false)
	@NotBlank
	@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = NAME_IS_NOT_VALID)
	String firstName;

	@Schema(description = "firstName", example = "Kook", nullable = false)
	@NotBlank
	@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = LAST_NAME_IS_NOT_VALID)
	String lastName;
}
