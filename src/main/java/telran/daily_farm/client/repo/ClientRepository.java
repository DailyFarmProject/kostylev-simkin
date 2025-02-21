package telran.daily_farm.client.repo;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.Client;

public interface ClientRepository extends JpaRepository<Client, UUID> {

    boolean existsByEmail(String email);

    Optional<Client> findByEmail(String username);

    Optional<Client> findById(UUID id);
}
