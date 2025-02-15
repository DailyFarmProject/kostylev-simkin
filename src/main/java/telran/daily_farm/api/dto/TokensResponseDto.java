package telran.daily_farm.api.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokensResponseDto {
	

	private String accessToken ;
	
    private String refreshToken ;

}
