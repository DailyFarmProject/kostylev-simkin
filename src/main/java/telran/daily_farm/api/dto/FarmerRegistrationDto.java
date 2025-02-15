package telran.daily_farm.api.dto;


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
	
	@Valid
	AddressDto address;

	@Valid
	CoordinatesDto coordinates;

//	@NotNull(message = PAYPAL_DETAILS_IS_REQUIRED)
	@Valid
	PayPalConfigDto paypalDetails;

}
