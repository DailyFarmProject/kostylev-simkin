package telran.daily_farm.location.dto;

import static telran.daily_farm.api.messages.ErrorMessages.*;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class CoordinatesDto {

	@Schema(description = "latitude", example = "32.800223849999995",  nullable = false)
	@NotNull(message = LATITUDE_REQUIRED)
	@Min(value = -90, message = INVALID_LATITUDE)
	@Max(value = 90, message = INVALID_LATITUDE)
	private Double latitude;

	@Schema(description = "longitude", example = "35.522077311215256",  nullable = false)
	@NotNull(message = LONGITUDE_REQUIRED)
	@Min(value = -180, message = INVALID_LONGITUDE)
	@Max(value = 180, message = INVALID_LONGITUDE)
	private Double longitude;

}
