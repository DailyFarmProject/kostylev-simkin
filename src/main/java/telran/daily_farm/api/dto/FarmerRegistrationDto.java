package telran.daily_farm.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class FarmerRegistrationDto extends BaseUser {
	
	@Schema(description = "address")
	@Valid
	AddressDto address;

	@Schema(description = "coordinates")
	@Valid
	CoordinatesDto coordinates;

	@Schema(description = "paypalDetails")
//	@NotNull(message = PAYPAL_DETAILS_IS_REQUIRED)
	@Valid
	PayPalConfigDto paypalDetails;

}
