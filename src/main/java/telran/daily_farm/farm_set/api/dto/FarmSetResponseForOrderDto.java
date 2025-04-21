package telran.daily_farm.farm_set.api.dto;


import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import telran.daily_farm.farm_set.entity.FarmSet;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class FarmSetResponseForOrderDto {


	private UUID farmSetId;
	private UUID farmerId;
	private double price;
	
	public static FarmSetResponseForOrderDto fromFarmSet(FarmSet farmSet) {
		
		return FarmSetResponseForOrderDto.builder()
				.farmerId(farmSet.getFarmerId())
				.farmSetId(farmSet.getId())
				.price(farmSet.getPrice())
				.build();
	}
	
}
