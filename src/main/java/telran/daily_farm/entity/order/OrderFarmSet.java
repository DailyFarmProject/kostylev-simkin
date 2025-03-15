package telran.daily_farm.entity.order;

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
import telran.daily_farm.entity.Customer;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.entity.farm_set.FarmSet;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "farm_set_order")
public class OrderFarmSet {
	@Id
	@UuidGenerator
	@GeneratedValue
	private UUID id;
	
	@Column(nullable = false)
	private double sumOfOrder;
	
	@Column(nullable = false)
	private boolean isStatusClosed;
	

	@Column(nullable = false)
	private boolean isPaid;
	
	@ManyToOne
	@JoinColumn(name = "farmer_id", nullable = false)
	private Farmer farmer;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "farmset_id", nullable = false)
	private FarmSet farmSet;
	
}
