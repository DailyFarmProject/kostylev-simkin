package telran.daily_farm.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class CustomerRegistrationDto extends BaseUser {

    @Schema(description = "Customer's city (optional)", example = "Tel Aviv")
    private String city;

    @Schema(description = "Customer's coordinates (optional, used for delivery)")
    private CoordinatesDto coordinates;
}
