//package telran.daily_farm.order.controller;
//
//
//import org.springframework.http.*;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RestController;
//
//import lombok.RequiredArgsConstructor;
//
//import lombok.extern.slf4j.Slf4j;
//import telran.daily_farm.order.api.dto.CreateOrderRequestDto;
//import telran.daily_farm.order.api.dto.CreateOrderResponseDto;
//import telran.daily_farm.order.api.dto.FarmSetRequestForOrderDto;
//import telran.daily_farm.order.service.ProxyOrderService;
//import telran.daily_farm.security.UserDetailsWithId;
//
//import static telran.daily_farm.api.ApiConstants.*;
//
//@RestController
//@Slf4j
//@RequiredArgsConstructor
//public class OrderController {
//
//	private final ProxyOrderService orderServise;
//
//	    
//    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
//	@PostMapping(CREATE_ORDER)
//	public ResponseEntity<CreateOrderResponseDto> createOrder(@RequestBody FarmSetRequestForOrderDto requestDto,
//			@AuthenticationPrincipal UserDetailsWithId user, @RequestHeader("Authorization") String token) {
//    	CreateOrderResponseDto response= orderServise.createOrder(requestDto , user.getId());
//		return  ResponseEntity.ok(response);
//	}
//	
//
//}
