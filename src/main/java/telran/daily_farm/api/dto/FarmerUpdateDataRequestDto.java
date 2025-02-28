package telran.daily_farm.api.dto;


import static telran.daily_farm.api.messages.ErrorMessages.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FarmerUpdateDataRequestDto{
	
	@Schema(description = "phone", example = "999888585")
	@Pattern(regexp = "\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,4}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}", message = PHONE_NUMBER_IS_NOT_VALID)
	String phone;

	@Schema(description = "company name", example = "My FARM")
	String company;
	
	@Schema(description = "coordinates")
	@Valid
	CoordinatesDto coordinates;

	
//	@Schema(description = "email", example = "petr@mail.mail")
//	@Email( message = EMAIL_IS_NOT_VALID)
//	String email;
//	@Schema(description = "firstName", example = "Petr")
//	@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = NAME_IS_NOT_VALID)
//	String firstName;
//	
//	@Schema(description = "lastName", example = "Pervyi")
//	@Pattern(regexp = "[A-Z][a-z]{1,20}([-\\s][A-Z][a-z]{1,20})*", message = LAST_NAME_IS_NOT_VALID)
//	String lastName;	
//	@Schema(description = "address")
//	@Valid
//	AddressDto address;	
//	@Schema(description = "paypalDetails")
//	@NotNull(message = PAYPAL_DETAILS_IS_REQUIRED)
//	@Valid
//	PayPalConfigDto paypalDetails;

}
