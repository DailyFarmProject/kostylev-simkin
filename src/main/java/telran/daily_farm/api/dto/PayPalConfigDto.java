package telran.daily_farm.api.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public class PayPalConfigDto {

	@Schema(description = "paypalClientId", example = "id1")
//	@NotBlank(message = PAYPAL_CLIENT_ID_INVALID)
//	@Pattern(regexp = "^[A-Za-z0-9_-]{50,100}$", message = PAYPAL_CLIENT_ID_INVALID)
	private String paypalClientId;

	@Schema(description = "paypalSecret", example = "paypalSecret111")
//	@NotBlank(message = PAYPAL_SECRET_INVALID)
//	@Pattern(regexp = "^[A-Za-z0-9]{30,60}$", message = PAYPAL_SECRET_INVALID)
	private String paypalSecret;
}
