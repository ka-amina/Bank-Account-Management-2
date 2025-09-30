package services;

import entities.Client;
import repositories.ClientRepository;
import repositories.ClientRepositoryImpl;

import java.util.UUID;

public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService() {
        this.clientRepository = new ClientRepositoryImpl();
    }

    public Client createClient(String firstName, String lastName, String email, String cin, String address, String phoneNumber, int created_by) {
        Client client = new Client(firstName, lastName, email, cin, phoneNumber, address, created_by);
        if (clientRepository.createClient(client)) {
            return client;
        }
        return null;
    }

}
