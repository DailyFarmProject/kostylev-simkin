package telran.daily_farm.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class PayPalConfig {
	    private String paypalClientId;
	    private String paypalSecret;
}
