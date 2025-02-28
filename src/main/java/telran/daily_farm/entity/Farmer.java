package telran.daily_farm.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import telran.daily_farm.api.dto.FarmerRegistrationDto;

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
	String company;

	@Column(nullable = false)
	String phone;

	Double balance;

	@OneToOne(mappedBy = "farmer", cascade = CascadeType.ALL)
	Coordinates coordinates;

	@OneToOne(mappedBy = "farmer", cascade = CascadeType.ALL)
	FarmerCredential credential;

//	@Embedded
//	PayPalConfigDto paypalDetails;
//	@Column(nullable = false)
//	String firstName;
//	@Column(nullable = false)
//	String lastName;
//	@OneToOne(mappedBy = "farmer", cascade = CascadeType.ALL)
//	Address address;


	
	   public Farmer(UUID id) { 
	        this.id = id;
	    }

	public static Farmer of(FarmerRegistrationDto dto) {
		return Farmer.builder().email(dto.getEmail())
				.phone(dto.getPhone())
				.company(dto.getCompany())
				.build();
	}

}
