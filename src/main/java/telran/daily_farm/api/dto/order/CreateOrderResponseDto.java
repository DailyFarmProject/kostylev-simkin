package telran.daily_farm.api.dto.order;



import lombok.AllArgsConstructor;
import lombok.Setter;
import telran.daily_farm.entity.farm_set.FarmSet;

@AllArgsConstructor
@Setter
public class CreateOrderResponseDto {
	
	private double sumOfOrder;
	private FarmSet farmSet;
	
	private String message;
	private String paypalLink;

}
