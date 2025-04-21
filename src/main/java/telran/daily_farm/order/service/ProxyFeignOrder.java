package telran.daily_farm.order.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import telran.daily_farm.order.api.dto.CreateOrderRequestDto;
import telran.daily_farm.order.api.dto.CreateOrderResponseDto;

@FeignClient(name = "orderService", url = "http://localhost:8081/order")
public interface ProxyFeignOrder {

	@PostMapping
	CreateOrderResponseDto orderCreate(@RequestBody CreateOrderRequestDto request);
}
