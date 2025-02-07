package telran.daily_farm.api.dto;

import static daily_farm.messages.ErrorMessages.*;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class AddressDto {
	
	@Pattern(regexp = "austria|belgium|bulgaria|croatia|cyprus|czech republic|denmark|estonia|finland|france|germany|greece|hungary|ireland|italy|latvia|lithuania|luxembourg|"
			+ "malta|netherlands|poland|portugal|romania|slovakia|slovenia|spain|sweden", message = NOT_VALID_COUNTRY)
	String country;
	
	@Pattern(regexp = "^[A-Z][a-z]+(?:[ -][A-Z][a-z]+)*$", message = NOT_VALID_CITY)
	String city;
	String street;
	

}
