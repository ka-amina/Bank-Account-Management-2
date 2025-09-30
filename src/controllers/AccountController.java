package controllers;

import entities.Account;
import entities.User;
import handlers.AccountHandler;

public class AccountController {
    private final AccountHandler handler = new AccountHandler();

    public Account createAccount(User user) {
        return handler.createAccount(user);
    }

    public void listMyAccounts(User user) {
        handler.listMyAccounts(user);
    }
}