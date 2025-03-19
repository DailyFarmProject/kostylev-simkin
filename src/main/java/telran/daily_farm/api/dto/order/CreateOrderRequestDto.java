package telran.daily_farm.api.dto.order;



import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateOrderRequestDto {
	
	private UUID customerId;
	
	private UUID farmSetId;
}
