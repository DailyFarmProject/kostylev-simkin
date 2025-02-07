package telran.daily_farm.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto extends BaseUser{

	AddressDto address;
	
}
