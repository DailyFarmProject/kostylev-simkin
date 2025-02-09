package telran.daily_farm.api.dto;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AddressDto {
	
	 @NotBlank(message = COUNTRY_REQUIRED)
	    private String country;

	    @NotBlank(message = REGION_REQUIRED)
	    private String region;

	    @NotBlank(message = CITY_REQUIRED)
	    private String city;

	    @NotBlank(message = POSTAL_CODE_REQUIRED)
	    @Pattern(regexp = "\\d{4,10}", message = POSTAL_CODE_REQUIRED)
	    private String postalCode;

	    private String street;

	    @Size(max = 10, message = HOUSE_NUMBER_TOO_LONG)
	    private String houseNumber;

	    private String placeName;  

	    @Size(max = 255, message = ADDITIONAL_INFO_TOO_LONG)
	    private String additionalInfo;
	

}
