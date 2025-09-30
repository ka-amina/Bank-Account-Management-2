package handlers;

import entities.User;
import services.AuthService;

import java.util.Scanner;

public class ProfileHandler {
    private final Scanner sc = new Scanner(System.in);
    private final AuthService authService = new AuthService();

    public void updateProfile(User user) {
        System.out.println("--- Update Profile ---");
        System.out.print("New name (leave blank to keep current): ");
        String name = sc.nextLine().trim();
        if (name.isEmpty()) name = user.getName();

        System.out.print("New email (leave blank to keep current): ");
        String email = sc.nextLine().trim();
        if (email.isEmpty()) email = user.getEmail();

        boolean change = authService.updateProfile(user.getId(), name, email);
        if (change) {
            user.setName(name);
            user.setEmail(email);
            System.out.println("Profile updated successfully.");
        } else {
            System.out.println("Failed to update profile (email may already exist).");
        }
    }
}