package controllers;

import entities.Client;
import entities.User;
import handlers.ClientHandler;

public class ClientController {
    private final ClientHandler clientHandler;
    public ClientController(){
        this.clientHandler= new ClientHandler();
    }

    public Client createClient(User user){
        return clientHandler.createClient(user);
    }

}
