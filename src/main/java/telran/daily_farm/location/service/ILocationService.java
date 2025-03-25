package telran.daily_farm.location.service;

import telran.daily_farm.location.dto.*;
import telran.daily_farm.location.dto.AddressDto;
import telran.daily_farm.location.dto.CoordinatesDto;

public interface ILocationService {

	
	CoordinatesDto getCoordinatesFromAddress(AddressDto address);

	AddressDto getAddtessFromCoordinates(CoordinatesDto coordinates);
	
}
