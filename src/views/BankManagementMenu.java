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
    private final CreditController creditController = new CreditController();
    private final ClientService clientService = new ClientService();
    private final FeeRuleController feeRuleController = new FeeRuleController();

    public void showMenu(User user) {
        int choice;
        do {
            System.out.println("====================================================================");
            System.out.println("=                    Bank Management System                        =");
            System.out.println("=                    Welcome, " + user.getName() + " (" + user.getRole() + ")");
            System.out.println("====================================================================");

            showRoleBasedMenu(user.getRole());

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
                System.out.println("=== Logged in as Admin ===");
                System.out.println("=       1. View all credits                                        =");
                System.out.println("=       2. List fee rules                                          =");
                System.out.println("=       3. Add fee rule                                            =");
                System.out.println("=       4. Toggle fee rule                                         =");
                System.out.println("=       5. Update profile                                          =");
                System.out.println("=       6. Change password                                         =");
                System.out.println("=       0. Logout                                                  =");
                break;
            case MANAGER:
                System.out.println("=== Logged in as Manager ===");
                System.out.println("=       1. View pending transfers                                  =");
                System.out.println("=       2. Approve transfer                                        =");
                System.out.println("=       3. View pending credits                                    =");
                System.out.println("=       4. Approve credit                                          =");
                System.out.println("=       5. Reject credit                                           =");
                System.out.println("=       6. Transaction history                                     =");
                System.out.println("=       7. List fee rules                                          =");
                System.out.println("=       8. Add fee rule                                            =");
                System.out.println("=       9. Toggle fee rule                                         =");
                System.out.println("=       10. Update profile                                         =");
                System.out.println("=       11. Change password                                        =");
                System.out.println("=       0. Logout                                                  =");
                break;
            case TELLER:
                System.out.println("=== Logged in as Teller ===");
                System.out.println("=       1. Create client                                           =");
                System.out.println("=       2. Create Account                                          =");
                System.out.println("=       3. List My Accounts                                        =");
                System.out.println("=       4. Close Account                                           =");
                System.out.println("=       5. Deposit                                                 =");
                System.out.println("=       6. Withdraw                                                =");
                System.out.println("=       7. Transfer                                                =");
                System.out.println("=       8. Transaction history                                     =");
                System.out.println("=       9. Request credit                                          =");
                System.out.println("=      10. Credit follow-up                                        =");
                System.out.println("=      11. Update profile                                          =");
                System.out.println("=      12. Change password                                         =");
                System.out.println("=       0. Logout                                                  =");
                break;
            case AUDITOR:
                System.out.println("=== Logged in as Auditor ===");
                System.out.println("=       1. View all transactions                                   =");
                System.out.println("=       2. Transaction history                                     =");
                System.out.println("=       3. Credit follow-up                                        =");
                System.out.println("=       4. View all credits                                        =");
                System.out.println("=       5. Update profile                                          =");
                System.out.println("=       6. Change password                                         =");
                System.out.println("=       0. Logout                                                  =");
                break;
        }
    }

    private void handleMenuChoice(int choice, User user) {
        if (choice == 0) {
            System.out.println("Logging out. Goodbye, " + user.getName() + "!");
            return;
        }

        switch (user.getRole()) {
            case ADMIN:
                handleAdminChoice(choice, user);
                break;
            case MANAGER:
                handleManagerChoice(choice, user);
                break;
            case TELLER:
                handleTellerChoice(choice, user);
                break;
            case AUDITOR:
                handleAuditorChoice(choice, user);
                break;
            default:
                System.out.println("Invalid role or choice.");
        }
    }

    private void handleAdminChoice(int choice, User user) {
        switch (choice) {
            case 1:
                creditController.showAllCredits();
                break;
            case 2:
                feeRuleController.listRules();
                break;
            case 3:
                feeRuleController.addRule();
                break;
            case 4:
                feeRuleController.toggleRule();
                break;
            case 5:
                profileController.updateProfile(user);
                break;
            case 6:
                passwordController.changePassword(user);
                break;
            default:
                System.out.println("Invalid option, please try again.");
                break;
        }
    }

    private void handleManagerChoice(int choice, User user) {
        switch (choice) {
            case 1:
                transactionController.showPendingTransfers();
                break;
            case 2:
                transactionController.approveTransfer();
                break;
            case 3:
                creditController.showPendingCredits();
                break;
            case 4:
                creditController.approveCredit(user);
                break;
            case 5:
                creditController.rejectCredit();
                break;
            case 6:
                transactionController.showTransactionHistory();
                break;
            case 7:
                feeRuleController.listRules();
                break;
            case 8:
                feeRuleController.addRule();
                break;
            case 9:
                feeRuleController.toggleRule();
                break;
            case 10:
                profileController.updateProfile(user);
                break;
            case 11:
                passwordController.changePassword(user);
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
            case 9:
                creditController.requestCredit(user);
                break;
            case 10:
                creditController.showCreditFollowUp();
                break;
            case 11:
                profileController.updateProfile(user);
                break;
            case 12:
                passwordController.changePassword(user);
                break;
            default:
                System.out.println("Invalid option, please try again.");
                break;
        }
    }

    private void handleAuditorChoice(int choice, User user) {
        switch (choice) {
            case 1:
                transactionController.showPendingTransfers();
                break;
            case 2:
                transactionController.showTransactionHistory();
                break;
            case 3:
                creditController.showCreditFollowUp();
                break;
            case 4:
                creditController.showAllCredits();
                break;
            case 5:
                profileController.updateProfile(user);
                break;
            case 6:
                passwordController.changePassword(user);
                break;
            default:
                System.out.println("Invalid option, please try again.");
                break;
        }
    }
}