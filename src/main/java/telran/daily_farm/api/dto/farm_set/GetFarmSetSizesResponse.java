package telran.daily_farm.api.dto.farm_set;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GetFarmSetSizesResponse {
	
	List<String> sizes;
}
