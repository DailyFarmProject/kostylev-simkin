package telran.daily_farm.order.service;

import java.util.UUID;

import telran.daily_farm.api.dto.order.CreateOrderRequestDto;
import telran.daily_farm.api.dto.order.CreateOrderResponseDto;
import telran.daily_farm.entity.order.OrderFarmSet;

public interface IOrderService {

	CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto, UUID id);

	void checkPendingPayments();

	void cancelOrder(OrderFarmSet order);
}
