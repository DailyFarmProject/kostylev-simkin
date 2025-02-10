package telran.daily_farm.location.service;

import telran.daily_farm.api.dto.AddressDto;
import telran.daily_farm.api.dto.CoordinatesDto;

public interface ILocationService {

	
	CoordinatesDto getCoordinatesFromAddress(AddressDto address);

	AddressDto getAddtessFromCoordinates(CoordinatesDto coordinates);
	
}
