package controllers;

import entities.User;
import handlers.PasswordHandler;

public class PasswordController {
    private final PasswordHandler handler = new PasswordHandler();

    public void changePassword(User user) {
        handler.changePassword(user);
    }
}