package telran.daily_farm.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RefreshTokenResponseDto {
	@Schema(description = "refreshToken", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI5OTc3YzFjOS0yODU3LTQ4MDItYjQ0Ni0zZmMzYTEyNmFhNzgiLCJlbWFpbCI6IjVAbWFpbC5tYWlsIiwiaWF0Ijo"
			+ "xNzM5NTYwMDY0LCJleHAiOjE3Mzk1NjAxODR9.4BWf9wrrvYJEnYEr4TA9JLE0YqlDfgLmiS0nwObTCOc")
	String accessToken;//typo
}
