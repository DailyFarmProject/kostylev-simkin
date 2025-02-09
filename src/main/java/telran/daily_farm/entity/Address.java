package telran.daily_farm.entity;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class Address {
	    private String country;
	    private String region;
	    private String city;
	    private String postalCode;
	    private String street;
	    private String houseNumber;
	    private String placeName;  
	    private String additionalInfo;
	

}
