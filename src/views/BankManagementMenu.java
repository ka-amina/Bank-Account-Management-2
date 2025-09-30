package views;

import controllers.AccountController;
import controllers.ClientController;
import controllers.PasswordController;
import controllers.ProfileController;
import entities.User;
import enums.UserRole;

import java.util.Scanner;

public class BankManagementMenu {
    private final Scanner sc = new Scanner(System.in);
    private final ClientController clientController = new ClientController();
    private final AccountController accountController = new AccountController();
    private final PasswordController passwordController = new PasswordController();
    private final ProfileController profileController = new ProfileController();

    public void showMenu(User user) {
        int choice;
        do {
            System.out.println("====================================================================");
            System.out.println("=                    Bank Management System                        =");
            System.out.println("=                    Welcome, " + user.getName() + " (" + user.getRole() + ")");
            System.out.println("====================================================================");

            showRoleBasedMenu(user.getRole());

            System.out.println("=       8. update profile                                          =");
            System.out.println("=       9. change password                                         =");
            System.out.println("=       0. Logout                                                  =");
            System.out.println("====================================================================");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();
            handleMenuChoice(choice, user);

        } while (choice != 0);
    }

    private void showRoleBasedMenu(UserRole role) {
        switch (role) {
            case ADMIN:
                System.out.println("logged in as an Admin");
                break;
            case MANAGER:
                System.out.println("logged in as an Manager");
                break;
            case TELLER:
                System.out.println("=       1. Create client                                           =");
                System.out.println("=       2. Create Account                                          =");
                System.out.println("=       3. List My Accounts                                        =");
                System.out.println("=       4. close Account                                           =");
                break;
            case AUDITOR:
                System.out.println("logged in as an Auditor");
                break;
        }
    }

    private void handleMenuChoice(int choice, User user) {
        if (choice == 0) {
            System.out.println("Logging out. Goodbye, " + user.getName() + "!");
            return;
        }
        if (choice == 8) {
            profileController.updateProfile(user);
            return;
        }
        if (choice == 9) {
            passwordController.changePassword(user);
            return;
        }
        switch (user.getRole()) {
            case ADMIN:
                handleAdminChoice(choice);
                break;
            case MANAGER:
                handleManagerChoice(choice);
                break;
            case TELLER:
                handleTellerChoice(choice, user);
                break;
            case AUDITOR:
                handleAuditorChoice(choice);
                break;
            default:
                System.out.println("Invalid role or choice.");
        }

    }

    private void handleAdminChoice(int choice) {
    }

    private void handleManagerChoice(int choice) {

    }

    private void handleTellerChoice(int choice, User user) {
        switch (choice) {
            case 1:
                clientController.createClient(user);
                break;
            case 2:
                accountController.createAccount(user);
                break;
            case 3:
                accountController.listMyAccounts(user);
                break;
            case 4:
                accountController.closeAccount(user);
                break;
            default:
                System.out.println("invalid option , please try again. ");
                break;
        }
    }

    private void handleAuditorChoice(int choice) {

    }
}

