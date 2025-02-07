package telran.daily_farm.api.dto;

import static daily_farm.messages.ErrorMessages.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class PayPalConfigDto {

	@NotBlank(message = PAYPAL_CLIENT_ID_INVALID)
	@Pattern(regexp = "^[A-Za-z0-9_-]{50,100}$", message = PAYPAL_CLIENT_ID_INVALID)
	private String paypalClientId;

	@NotBlank(message = PAYPAL_SECRET_INVALID)
	@Pattern(regexp = "^[A-Za-z0-9]{30,60}$", message = PAYPAL_SECRET_INVALID)
	private String paypalSecret;
}
