package views;

import entities.User;
import handlers.LoginHandler;

public class Login {
    private LoginHandler loginHandler;

    public Login() {
        this.loginHandler = new LoginHandler();
    }

    public User showLoginMenu() {
        return loginHandler.login();
    }
}