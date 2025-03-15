package telran.daily_farm.order.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import telran.daily_farm.api.dto.order.CreateOrderResponseDto;

public interface IOrderService {

	ResponseEntity<CreateOrderResponseDto> createOrder(UUID customerId, UUID farmSetId);
	
	
}
