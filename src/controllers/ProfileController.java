package controllers;

import entities.User;
import handlers.ProfileHandler;

public class ProfileController {
    private final ProfileHandler handler = new ProfileHandler();

    public void updateProfile(User user) {
        handler.updateProfile(user);
    }
}