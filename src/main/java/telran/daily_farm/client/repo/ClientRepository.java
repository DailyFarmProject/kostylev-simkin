package telran.daily_farm.client.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.Client;


public interface ClientRepository  extends JpaRepository<Client, String>{

}
