package repositories;

import entities.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    boolean createClient(Client client);
    Optional<UUID> findIdByCin(String cin);
    Optional<UUID> findClientIdByUserId(int userId);
    List<Client> findAll();
}