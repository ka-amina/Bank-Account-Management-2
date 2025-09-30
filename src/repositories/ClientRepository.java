package repositories;


import entities.Client;

import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {
    boolean createClient(Client client);
    Optional<UUID> findIdByCin(String cin);
}
