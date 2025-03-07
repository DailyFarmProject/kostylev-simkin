package telran.daily_farm.api.dto.farm_set;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class FarmSetDto {

	String size;
	
	String category;
	
	String description;
	
	double price;
	
	int availibleCount;
	
	boolean abailible;
	
	 @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	LocalDateTime pickupTimeStart;
	
	 @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	LocalDateTime pickupTimeEnd;
	
	 
	
}
