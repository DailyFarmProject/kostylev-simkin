package telran.daily_farm.entity;





import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
import telran.daily_farm.api.dto.CoordinatesDto;
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
    @GeneratedValue
    @UuidGenerator
	UUID id;
	
	@Column(unique = true, nullable = false)
	String email;

	@Column(nullable = false)
	String firstName;
	
	@Column(nullable = false)
	String lastName;
	
	@Column(nullable = false)
	String phone;
	
	@Column(nullable = false)
	String password;
	
	@Embedded
	AddressDto address;
	
	@Embedded
	CoordinatesDto coordinates;
	
	@Embedded
	PayPalConfigDto paypalDetails;
	
	Double balance;
	
	
	public static Farmer of(FarmerDto dto) {
		return Farmer.builder().email(dto.getEmail())
						.firstName(dto.getFirstName())
						.lastName(dto.getLastName())
						.phone(dto.getPhone())
						.address(dto.getAddress())
						.coordinates(dto.getCoordinates())
						.paypalDetails(dto.getPaypalDetails())
						.build();
	}
	
	public FarmerDto build() {
		return FarmerDto.builder().email(email)
						.lastName(lastName)
						.firstName(firstName)
						.phone(phone)
						.password(password)
						.address(address)
						.paypalDetails(paypalDetails)
						.build();
	}
}
