package telran.daily_farm.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@ToString
public class ClientRegistrationDto extends BaseUser {

    @Schema(description = "Client's city (optional)", example = "Tel Aviv")
    private String city;

    @Schema(description = "Client's coordinates (optional, used for delivery)")
    private CoordinatesDto coordinates;
}
