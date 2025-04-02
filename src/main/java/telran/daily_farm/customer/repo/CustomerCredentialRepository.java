package telran.daily_farm.customer.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.customer.entity.Customer;
import telran.daily_farm.customer.entity.CustomerCredential;

public interface CustomerCredentialRepository extends JpaRepository<CustomerCredential, UUID> {

    CustomerCredential findByCustomer(Customer customer);
}
