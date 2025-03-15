package telran.daily_farm.order.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.order.OrderFarmSet;

public interface OrderRepository extends JpaRepository<OrderFarmSet, UUID>{

}
