package telran.daily_farm.order.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import telran.daily_farm.api.dto.order.CreateOrderRequestDto;
import telran.daily_farm.api.dto.order.CreateOrderResponseDto;
import telran.daily_farm.order.service.OrderService;

import static telran.daily_farm.api.ApiConstants.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

	private final OrderService orderServise;

	
    @Value("${paypal.api-url}")
    private String apiUrl;
    
	@PostMapping(CREATE_ORDER)
	public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto requestDto) {
		CreateOrderResponseDto response = orderServise.createOrder(requestDto);
		
		log.info("Order controller: response ( sum - {} , link - {}", response.getSumOfOrder(), response.getPaymentLink());
		
		return  ResponseEntity.ok(response);
	}
	

}
