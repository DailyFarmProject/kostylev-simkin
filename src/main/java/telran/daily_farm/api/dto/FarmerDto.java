package telran.daily_farm.api.dto;

import static daily_farm.messages.ErrorMessages.*;
import jakarta.validation.constraints.NotNull;
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
public class FarmerDto extends BaseUser{

	@NotNull(message = ADDRESS_FIELD_IS_REQUIRED)
	AddressDto address;
	
//	@NotNull(message = PAYPAL_DETAILS_IS_REQUIRED)
	PayPalConfigDto paypalDetails;



	
	
	
}
