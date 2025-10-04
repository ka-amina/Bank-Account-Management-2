package views;

import controllers.*;
import entities.Client;
import entities.User;
import enums.UserRole;
import services.ClientService;

import java.util.*;
import java.util.Scanner;

public class BankManagementMenu {
    private final Scanner sc = new Scanner(System.in);
    private final ClientController clientController = new ClientController();
    private final AccountController accountController = new AccountController();
    private final PasswordController passwordController = new PasswordController();
    private final ProfileController profileController = new ProfileController();
    private final TransactionController transactionController = new TransactionController();
    private final ClientService clientService = new ClientService();

    public void showMenu(User user) {
        int choice;
        do {
            System.out.println("====================================================================");
            System.out.println("=                    Bank Management System                        =");
            System.out.println("=                    Welcome, " + user.getName() + " (" + user.getRole() + ")");
            System.out.println("====================================================================");

            showRoleBasedMenu(user.getRole());

            System.out.println("=       8. Update profile                                         =");
            System.out.println("=       9. Change password                                        =");
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
                System.out.println("=== Logged in as Manager ===");
                System.out.println("=       1. View pending transfers                                  =");
                System.out.println("=       2. Approve transfer                                        =");
                System.out.println("=       3. Transaction history                                     =");
                break;
            case TELLER:
                System.out.println("=       1. Create client                                           =");
                System.out.println("=       2. Create Account                                          =");
                System.out.println("=       3. List My Accounts                                        =");
                System.out.println("=       4. Close Account                                           =");
                System.out.println("=       5. Deposit                                                 =");
                System.out.println("=       6. Withdraw                                                =");
                System.out.println("=       7. Transfer                                                =");
                System.out.println("=       8. Transaction history                                     =");
                break;
            case AUDITOR:
                System.out.println("=== Logged in as Auditor ===");
                System.out.println("=       1. View all transactions                                   =");
                System.out.println("=       2. Transaction history                                     =");
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
        switch (choice) {
            case 1:
                transactionController.showPendingTransfers();
                break;
            case 2:
                transactionController.approveTransfer();
                break;
            case 3:
                transactionController.showTransactionHistory();
                break;
            default:
                System.out.println("Invalid option, please try again.");
                break;
        }
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
                accountController.listMyAccounts();
                break;
            case 4:
                accountController.closeAccount();
                break;
            case 5:
                transactionController.deposit();
                break;
            case 6:
                transactionController.withdraw();
                break;
            case 7:
                List<Client> clients = clientService.getAllClients();
                if (clients.isEmpty()) {
                    System.out.println("No clients found. Create a client first.");
                    break;
                }

                System.out.println("\n=== Select Client ===");
                for (int i = 0; i < clients.size(); i++) {
                    Client c = clients.get(i);
                    System.out.printf("%d. %s %s (CIN: %s)\n",
                            i + 1, c.getFirstName(), c.getLastName(), c.getCin());
                }

                System.out.print("Enter client number: ");
                try {
                    int choix = Integer.parseInt(sc.nextLine().trim());
                    if (choix >= 1 && choix <= clients.size()) {
                        transactionController.transfer(clients.get(choix - 1).getClientId());
                    } else {
                        System.out.println("Invalid selection.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input.");
                }
                break;
            case 8:
                transactionController.showTransactionHistory();
                break;
            default:
                System.out.println("Invalid option, please try again.");
                break;
        }
    }

    private void handleAuditorChoice(int choice) {
        switch (choice) {
            case 1:
                transactionController.showPendingTransfers();
                break;
            case 2:
                transactionController.showTransactionHistory();
                break;
            default:
                System.out.println("Invalid option, please try again.");
                break;
        }
    }
}