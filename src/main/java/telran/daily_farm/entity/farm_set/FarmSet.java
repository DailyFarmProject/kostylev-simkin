package telran.daily_farm.entity.farm_set;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.daily_farm.api.dto.farm_set.FarmSetResponseDto;
import telran.daily_farm.entity.Farmer;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "farm_set")
public class FarmSet {

	@Id
	@GeneratedValue
	@UuidGenerator
	UUID id;

	@Column(nullable = false)
	String description;

	@Column(nullable = false)
	private double price;

	@Column(nullable = false)
	int availibleCount;

	@Column(nullable = false)
	boolean abailible;

	@Column(nullable = false)
	LocalDateTime pickupTimeStart;

	@Column(nullable = false)
	LocalDateTime pickupTimeEnd;

	@ManyToOne
	@JoinColumn(name = "farmer_id", nullable = false)
	Farmer farmer;

	@ManyToOne
	@JoinColumn(name = "size_id", nullable = false)
	FarmSetSize size;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	FarmSetCategory category;



	public FarmSet(UUID id) {
			this.id = id;
		}

	public static FarmSetResponseDto buildFromEntity(FarmSet fs) {
		return FarmSetResponseDto.builder().category(fs.category.category).size(fs.size.size).id(fs.id)
				.availibleCount(fs.availibleCount).description(fs.description).price(fs.price)
				.pickupTimeStart(fs.pickupTimeStart).pickupTimeEnd(fs.pickupTimeEnd)

				.build();
	}
}
