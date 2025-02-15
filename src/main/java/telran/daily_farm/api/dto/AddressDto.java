package telran.daily_farm.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter

public class AddressDto {

	@Schema(description = "country", example = "Israel")
	@NotBlank(message = COUNTRY_REQUIRED)
	@Pattern(regexp = "^[\\p{L}][\\p{L}\\s-]{1,54}[\\p{L}]$", message = NOT_VALID_COUNTRY)
	private String country;

//	    @NotBlank(message = REGION_REQUIRED)
	@Schema(description = "region", example = "North District")
	@Pattern(regexp = "^(?!\\s*$)[\\p{L}\\s.'-]{3,50}$", message = NOT_VALID_REGION)
	private String region;

	@Schema(description = "city", example = "Tiberias")
	@NotBlank(message = CITY_REQUIRED)
	@Pattern(regexp = "^(?!\\s*$)[\\p{L}\\s.'-]{2,50}$", message = NOT_VALID_CITY)
	private String city;

	@Schema(description = "postalCode", example = "1410502")
	@Pattern(regexp = "\\d{4,10}", message = POSTAL_CODE_REQUIRED)
	private String postalCode;

	@Schema(description = "street", example = "Alhadif")
	@Pattern(regexp = "^(?!\\s*$)[\\p{L}\\d\\s.,'’\"-]{3,100}$", message = NOT_VALID_STREET)
	private String street;

	@Schema(description = "houseNumber", example = "100")
	@Size(max = 10, message = HOUSE_NUMBER_TOO_LONG)
	private String houseNumber;

	@Schema(description = "placeName", example = "Bank Leumi")
	private String placeName;

	@Schema(description = "additionalInfo", example = "Near bus station")
	@Size(max = 255, message = ADDITIONAL_INFO_TOO_LONG)
	private String additionalInfo;
}
