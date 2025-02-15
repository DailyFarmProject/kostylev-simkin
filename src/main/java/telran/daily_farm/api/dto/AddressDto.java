package telran.daily_farm.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.*;

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

	@NotBlank(message = COUNTRY_REQUIRED)
	@Pattern(regexp = "^[\\p{L}][\\p{L}\\s-]{1,54}[\\p{L}]$", message = NOT_VALID_COUNTRY)
	private String country;
	
//	    @NotBlank(message = REGION_REQUIRED)
	@Pattern(regexp = "^(?!\\s*$)[\\p{L}\\s.'-]{3,50}$", message = NOT_VALID_REGION)
	private String region;

	@NotBlank(message = CITY_REQUIRED)
	@Pattern(regexp = "^(?!\\s*$)[\\p{L}\\s.'-]{2,50}$", message = NOT_VALID_CITY)
	private String city;

	// @NotBlank(message = POSTAL_CODE_REQUIRED)
	@Pattern(regexp = "\\d{4,10}", message = POSTAL_CODE_REQUIRED)
	private String postalCode;

	@Pattern(regexp = "^(?!\\s*$)[\\p{L}\\d\\s.,'’\"-]{3,100}$", message = NOT_VALID_STREET)
	private String street;

	@Size(max = 10, message = HOUSE_NUMBER_TOO_LONG)
	private String houseNumber;

	private String placeName;

	@Size(max = 255, message = ADDITIONAL_INFO_TOO_LONG)
	private String additionalInfo;

}
