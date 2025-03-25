package telran.daily_farm.order.api.dto;



import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateOrderRequestDto {
	
	
	
	private UUID farmSetId;
}
