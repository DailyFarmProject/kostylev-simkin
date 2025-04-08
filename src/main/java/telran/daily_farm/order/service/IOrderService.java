package telran.daily_farm.order.service;

import java.util.UUID;

import telran.daily_farm.order.api.dto.CreateOrderResponseDto;
import telran.daily_farm.order.api.dto.FarmSetRequestForOrderDto;

public interface IOrderService {

	CreateOrderResponseDto createOrder(FarmSetRequestForOrderDto requestDto, UUID id);

}
