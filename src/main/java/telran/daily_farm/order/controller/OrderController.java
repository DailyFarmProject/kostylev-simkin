package telran.daily_farm.order.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import telran.daily_farm.api.dto.order.CreateOrderResponseDto;
import telran.daily_farm.order.service.OrderService;

import static telran.daily_farm.api.ApiConstants.*;

@RestController
public class OrderController {

	@Autowired
	OrderService orderServise;
	
	
	@PostMapping(CREATE_ORDER)
	public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody UUID customerId,@RequestBody UUID farmSetId) {
	
		return orderServise.createOrder(customerId, farmSetId); 
	}
	
}
