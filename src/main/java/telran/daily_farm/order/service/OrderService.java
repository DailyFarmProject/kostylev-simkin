package telran.daily_farm.order.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import telran.daily_farm.api.dto.order.CreateOrderResponseDto;
import telran.daily_farm.customer.repo.CustomerRepository;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.entity.farm_set.FarmSet;
import telran.daily_farm.entity.order.OrderFarmSet;
import telran.daily_farm.farm_set.repo.FarmSetRepository;
import telran.daily_farm.farmer.repo.FarmerRepository;
import telran.daily_farm.order.repo.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderService  implements IOrderService{

	private final FarmSetRepository farmSetRepo;
	private final CustomerRepository customerRepo;
	private final FarmerRepository farmerRepo;
	private final OrderRepository orderRepo;
	
	
	
	@Override
	@Transactional
	public ResponseEntity<CreateOrderResponseDto> createOrder(UUID customerId, UUID farmSetId) {
		
		FarmSet farmSet = farmSetRepo.findById(farmSetId).get();
		Farmer farmer =  farmSet.getFarmer();
		
		if(farmSet.getAvailibleCount() == 0)
			return ResponseEntity.notFound().build();
		
		farmSet.setAvailibleCount(farmSet.getAvailibleCount()-1);
		OrderFarmSet orderFarmSer = OrderFarmSet.builder()
										.farmSet(farmSet)
										.customer(customerRepo.findById(customerId).get())
										.farmer(farmer)
										.isPaid(false)
										.isStatusClosed(false)
										.sumOfOrder(farmSet.getPrice())
										.build();
		
		orderRepo.save(orderFarmSer);
			
		CreateOrderResponseDto response = new CreateOrderResponseDto(orderFarmSer.getSumOfOrder(), farmSet, "message", "paypalLink");
		
		
		return ResponseEntity.ok(response);
	}

}
