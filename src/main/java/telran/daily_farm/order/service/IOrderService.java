package telran.daily_farm.order.service;

import java.util.UUID;

import telran.daily_farm.order.api.dto.CreateOrderRequestDto;
import telran.daily_farm.order.api.dto.CreateOrderResponseDto;
import telran.daily_farm.order.entity.OrderFarmSet;

public interface IOrderService {

	CreateOrderResponseDto createOrder(CreateOrderRequestDto requestDto, UUID id);

	void checkPendingPayments();

	void cancelOrder(OrderFarmSet order);
}
