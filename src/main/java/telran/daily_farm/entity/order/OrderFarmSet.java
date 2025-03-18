package telran.daily_farm.entity.order;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import telran.daily_farm.api.dto.order.OrderStatus;
import telran.daily_farm.entity.Customer;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.entity.farm_set.FarmSet;
import telran.daily_farm.payment.entity.Payment;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "farm_set_orders")
public class OrderFarmSet {
	@Id
	@UuidGenerator
	@GeneratedValue
	private UUID id;
	
	 @Column(name = "created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;
	
	@Column(nullable = false)
	private double sumOfOrder;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OrderStatus status;


	@ManyToOne
	@JoinColumn(name = "farmer_id", nullable = false)
	private Farmer farmer;
	
	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private Customer customer;
	
	@ManyToOne
	@JoinColumn(name = "farmset_id", nullable = false)
	private FarmSet farmSet;

	@OneToOne(mappedBy = "orderFarmSet", cascade = CascadeType.ALL)
	private Payment payment;
	
	public OrderFarmSet(UUID id) {
		this.id = id;
	}
	
}
