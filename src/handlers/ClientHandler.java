package handlers;

import entities.Client;
import entities.User;
import services.ClientService;

import java.util.Scanner;

public class ClientHandler {
    private final ClientService clientService;
    private final Scanner sc = new Scanner(System.in);

    public ClientHandler() {
        this.clientService = new ClientService();
    }

    public void createClient(User loggedInUser) {
        System.out.println("====================================================================");
        System.out.println("=                     Create New Client                            =");
        System.out.println("====================================================================");

        System.out.print("Enter client first name: ");
        String firstName = sc.nextLine().trim();

        System.out.print("Enter client last name: ");
        String lastName = sc.nextLine().trim();

        System.out.print("Enter client email: ");
        String email = sc.nextLine().trim();

        System.out.print("Enter client CIN: ");
        String cin = sc.nextLine().trim();

        System.out.print("Enter client phone number: ");
        String phoneNumber = sc.nextLine().trim();

        System.out.print("Enter client address: ");
        String address = sc.nextLine().trim();


        Client client = clientService.createClient(
                firstName,
                lastName,
                email,
                cin,
                phoneNumber,
                address,
                loggedInUser.getId()
        );

        if (client != null) {
            System.out.println("====================================================================");
            System.out.println("Client created successfully!");
            System.out.println("Client ID: " + client.getClientId());
            System.out.println("Name: " + client.getFirstName() + " " + client.getLastName());
            System.out.println("CIN: " + client.getCin());
            System.out.println("Email: " + client.getEmail());
            System.out.println("====================================================================");
        } else {
            System.out.println("Error: Failed to create client. Please check if CIN or email already exists.");
        }
    }
}