//package telran.daily_farm.order.service;
//
//import java.util.UUID;
//
//
//import org.springframework.stereotype.Service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import telran.daily_farm.order.api.dto.CreateOrderRequestDto;
//import telran.daily_farm.order.api.dto.CreateOrderResponseDto;
//import telran.daily_farm.order.api.dto.FarmSetRequestForOrderDto;
//
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class ProxyOrderService implements IOrderService {
//
//
//	private final ProxyFeignOrder proxyOrderService;
//	
//	
//	@Override
//	public CreateOrderResponseDto createOrder(FarmSetRequestForOrderDto requestDto, UUID customerId) {
//		CreateOrderRequestDto request = new CreateOrderRequestDto(requestDto.getFarmSetId(), customerId);
//		
//		log.info("monolit createOrder");
//		
//		 return  proxyOrderService.orderCreate(request);
//	}
//
//
//}
