package controllers;

import entities.User;
import handlers.LoginHandler;

public class LoginController {
    private final LoginHandler loginHandler;

    public LoginController() {
        this.loginHandler = new LoginHandler();
    }

    public User showLoginMenu() {
        return loginHandler.login();
    }
}
