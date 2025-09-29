package handlers;

import entities.User;
import services.AuthService;

import java.util.Optional;
import java.util.Scanner;

public class LoginHandler {
    private AuthService authService;
    Scanner sc = new Scanner(System.in);

    public LoginHandler() {
        this.authService = new AuthService();
    }

    public User login() {
        System.out.println("====================================================================");
        System.out.println("=                           Login                                  =");
        System.out.println("====================================================================");

        System.out.println("Enter email: ");
        String email = sc.nextLine();

        System.out.println("Enter password: ");
        String password = sc.nextLine();

        Optional<User> userOptional = authService.login(email, password);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("Login successfully welcom," + user.getName());
            return user;
        } else {
            System.out.println("Invalid email or password. Please try again.");
            return null;
        }
    }
}
