package telran.daily_farm.location.service;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.location.dto.*;
import telran.daily_farm.location.dto.AddressDto;
import telran.daily_farm.location.dto.CoordinatesDto;
import telran.daily_farm.location.dto.LocationIqResponse;

@Service
@Slf4j
public class LocationIqService implements ILocationService {

	@Autowired
	private RestTemplate restTemplate;
	@Value("${location_iq.api.key}")
	private String apiKey;
	private static final String COORDINATES_FROM_ADDRESS_API_URL = "https://us1.locationiq.com/v1/search?key=";
	private static final String ADDRESS_FROM_COORDINATES_API_URL = "https://us1.locationiq.com/v1/reverse?key=";

	@Override
	public CoordinatesDto getCoordinatesFromAddress(AddressDto address) {

		String formattedAddress = getFormattedAddress(address);
		String url = COORDINATES_FROM_ADDRESS_API_URL + apiKey + "&q=" + formattedAddress
				+ "&accept-language=en&format=json&";
		try {
			ResponseEntity<List<LocationIqResponse>> response = restTemplate.exchange(url, HttpMethod.GET, null,
					new ParameterizedTypeReference<List<LocationIqResponse>>() {});
			
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				LocationIqResponse location = response.getBody().get(0);
				double importance = location.getImportance();
				System.out.println("importance" + importance);
				if(importance<0.4)
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ADDRESS_VALIDATION_FAILED + 1 );
				return  new CoordinatesDto(Double.valueOf(location.getLat()), Double.valueOf(location.getLon()));
			} else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ADDRESS_VALIDATION_FAILED+ 2 );
		} catch (RestClientException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, ADDRESS_VALIDATION_FAILED + 3);
		} 

	}

	private String getFormattedAddress(AddressDto address) {
		return String.format("%s, %s ,%s, %s, %s, %s", address.getCountry(),
							address.getRegion() == null ? "" : address.getRegion(),
							address.getPostalCode() == null ? "" : address.getPostalCode(), 
							address.getCity(),
							address.getStreet() == null ? "" : address.getStreet(),
							address.getHouseNumber() == null ? "" : address.getHouseNumber());
	}

	@Override
	public AddressDto getAddtessFromCoordinates(CoordinatesDto coordinates) {

		String url = ADDRESS_FROM_COORDINATES_API_URL + apiKey + "&lat=" + coordinates.getLatitude() + "&lon="
				+ coordinates.getLongitude() + "&format=json&";

		try {
			ResponseEntity<LocationIqResponse> response = restTemplate.exchange(url, HttpMethod.GET, null,
					LocationIqResponse.class);
			if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				LocationIqResponse location = response.getBody();
				AddressDto address = new AddressDto();
				Map<String, String> responseAddress = location.getAddress();
				for (Map.Entry<String, String> element : responseAddress.entrySet()) {
					String key = element.getKey();
					String value = element.getValue();
					switch (key) {
					case "country" -> address.setCountry(value);
					case "state" -> address.setRegion(value);
					case "town" -> address.setCity(value);
					case "road" -> address.setStreet(value);
					case "postcode" -> address.setPostalCode(value);
					case "house_number" -> address.setHouseNumber(value);
					};
				}
				return address;
			} else
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_COORDINATES );
			
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, WRONG_COORDINATES );
		}
	}

}
