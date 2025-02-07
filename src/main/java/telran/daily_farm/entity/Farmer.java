package telran.daily_farm.entity;





import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import telran.daily_farm.api.dto.AddressDto;
import telran.daily_farm.api.dto.FarmerDto;
import telran.daily_farm.api.dto.PayPalConfigDto;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@Builder
@Entity
@ToString
@Table(name = "farmers")
public class Farmer {
	
	@Id
	@Column(unique = true, nullable = false)
	String email;

	String firstName;
	
	String lastName;
	
	String phone;
	
	String password;
	
	@Embedded
	AddressDto address;
	@Embedded
	PayPalConfigDto paypalDetails;
	
	Double balance;
	
	
	public static Farmer of(FarmerDto dto) {
		return Farmer.builder().email(dto.getEmail()).firstName(dto.getFirstName()).lastName(dto.getLastName()).phone(dto.getPhone()).address(dto.getAddress())
				.paypalDetails(dto.getPaypalDetails())
				.build();
	}
	
	public FarmerDto build() {
		return FarmerDto.builder().email(email).lastName(lastName).firstName(firstName).phone(phone).password(password).address(address).paypalDetails(paypalDetails)
				.build();
	}
}
