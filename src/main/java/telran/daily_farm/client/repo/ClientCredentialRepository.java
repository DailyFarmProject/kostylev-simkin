package telran.daily_farm.client.repo;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import telran.daily_farm.entity.Client;
import telran.daily_farm.entity.ClientCredential;

public interface ClientCredentialRepository extends JpaRepository<ClientCredential, UUID> {

    ClientCredential findByClient(Client client);
}
