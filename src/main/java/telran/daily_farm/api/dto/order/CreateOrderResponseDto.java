package telran.daily_farm.api.dto.order;



import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateOrderResponseDto {
	
	private double sumOfOrder;
	
	private String message;

	private String paymentLink;

}
