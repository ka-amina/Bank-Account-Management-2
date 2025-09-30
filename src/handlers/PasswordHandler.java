package handlers;

import entities.User;
import services.AuthService;

import java.util.Scanner;

public class PasswordHandler {
    private final Scanner sc = new Scanner(System.in);
    private final AuthService authService = new AuthService();

    public void changePassword(User user) {
        System.out.println("--- Change Password ---");
        System.out.print("Current password: ");
        String current = sc.nextLine().trim();

        System.out.print("New password: ");
        String new1 = sc.nextLine().trim();

        System.out.print("Repeat new password: ");
        String new2 = sc.nextLine().trim();

        if (!new1.equals(new2)) {
            System.out.println("New passwords do not match.");
            return;
        }

        boolean change = authService.changePassword(user.getId(), current, new1);
        if (change) {
            System.out.println("Password changed successfully.");
            user.setPassword(new1);
        } else {
            System.out.println("Failed: current password incorrect.");
        }
    }
}