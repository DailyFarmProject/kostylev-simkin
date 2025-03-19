package telran.daily_farm.order.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.order.CreateOrderRequestDto;
import telran.daily_farm.api.dto.order.CreateOrderResponseDto;
import telran.daily_farm.api.dto.order.OrderStatus;
import telran.daily_farm.customer.repo.CustomerRepository;
import telran.daily_farm.entity.Farmer;
import telran.daily_farm.entity.farm_set.FarmSet;
import telran.daily_farm.entity.order.OrderFarmSet;
import telran.daily_farm.farm_set.repo.FarmSetRepository;
import telran.daily_farm.order.repo.OrderRepository;
import telran.daily_farm.payment.service.PaymentService;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService implements IOrderService {

	private final FarmSetRepository farmSetRepo;
	private final CustomerRepository customerRepo;
	private final OrderRepository orderRepo;
	private final PaymentService paymentService;

	@Override
	@Transactional
	public CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
		log.info("OrderService: Create order request");

		FarmSet farmSet = farmSetRepo.findById(requestDto.getFarmSetId()).get();
		log.info("OrderService : Create order. Got farmset from database description - {}", farmSet.getDescription());

		Farmer farmer = farmSet.getFarmer();
		log.info("OrderService : Create order:  Got farmer from database, email - {}", farmer.getEmail());

		if (farmSet.getAvailibleCount() == 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You ara late");

		farmSet.setAvailibleCount(farmSet.getAvailibleCount() - 1);
		OrderFarmSet orderFarmSet = OrderFarmSet.builder().farmSet(farmSet)
				.customer(customerRepo.findById(requestDto.getCustomerId()).get()).farmer(farmer)
				.status(OrderStatus.WAITING_FOR_PAYMENT).createdAt(LocalDateTime.now()).sumOfOrder(farmSet.getPrice()).build();
		log.info("OrderService : Create order - Created new order");

		orderRepo.save(orderFarmSet);
		log.info("OrderService : Create order - new order saved to database ");
		
		String approvalLink = paymentService.createPayment(orderFarmSet.getId(), orderFarmSet.getSumOfOrder(), "PayPal");
		log.info("OrderService : Create order - link for payment generated - {} ", approvalLink);
		
		CreateOrderResponseDto response = new CreateOrderResponseDto(orderFarmSet.getSumOfOrder(), "message", approvalLink);
		log.info("OrderService : Create order - Response for customer created!");
		
		return response;
	}

	@Override
	@Scheduled(fixedRate = 60000)
	@Transactional
	public void checkPendingPayments() {
		LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(1);
		List<OrderFarmSet> expiredOrders = orderRepo.findExpiredOrders(tenMinutesAgo);
		log.info("OrderService : Checking payment....", expiredOrders.size());
		for (OrderFarmSet order : expiredOrders) {
			log.info("Cancel order {} — not paid", order.getId());
			cancelOrder(order);

		}
	}

	
	@Override
	@Transactional
	public void cancelOrder(OrderFarmSet order) {
		FarmSet farmSet = order.getFarmSet();
		farmSet.setAvailibleCount(farmSet.getAvailibleCount() + 1);
		log.info("OrderService : Cancel order - add count to farmset");
		//paymentService.cancelPaymentOrder(order);
		order.setStatus(OrderStatus.CANCELLED);
		log.info("OrderService : Cancel order - changing status for order to CANCEL");
	}
}
