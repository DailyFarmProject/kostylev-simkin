package telran.daily_farm.farmer.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.daily_farm.farmer.api.dto.*;
import telran.daily_farm.farmer.api.dto.AddressDto;
import telran.daily_farm.farmer.entity.Farmer;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "farmers_addresses")
public class Address {

	@Id
	@GeneratedValue
	@UuidGenerator
	UUID id;

	@OneToOne
	@JoinColumn(name = "farmer_id", nullable = false)
	Farmer farmer;

	@Column(nullable = false)
	private String country;
	private String region;
	private String city;
	private String postalCode;
	private String street;
	private String houseNumber;
	private String placeName;
	private String additionalInfo;
	
	public static Address of(AddressDto dto) {
		return Address.builder().country(dto.getCountry())
				.region(dto.getRegion())
				.city(dto.getCity())
				.postalCode(dto.getPostalCode())
				.street(dto.getStreet())
				.houseNumber(dto.getHouseNumber())
				.placeName(dto.getPlaceName())
				.additionalInfo(dto.getAdditionalInfo())
				.build();
	}
	public void updateFromDto(AddressDto dto) {
	    this.country = dto.getCountry();
	    this.region = dto.getRegion();
	    this.city = dto.getCity();
	    this.postalCode = dto.getPostalCode();
	    this.street = dto.getStreet();
	    this.houseNumber = dto.getHouseNumber();
	    this.placeName = dto.getPlaceName();
	    this.additionalInfo = dto.getAdditionalInfo();
	}

}
