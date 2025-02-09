package telran.daily_farm.api.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString
public class FarmerDto extends BaseUser {
	
	AddressDto address;

	CoordinatesDto coordinates;

//	@NotNull(message = PAYPAL_DETAILS_IS_REQUIRED)
	PayPalConfigDto paypalDetails;

}
