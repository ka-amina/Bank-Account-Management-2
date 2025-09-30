package views;

import entities.User;
import enums.UserRole;
import handlers.ClientHandler;

import java.util.Scanner;

public class BankManagementMenu {
    private final Scanner sc = new Scanner(System.in);
    private final ClientHandler clientHandler= new ClientHandler();

    public void showMenu(User user) {
        int choice;
        do {
            System.out.println("====================================================================");
            System.out.println("=                    Bank Management System                        =");
            System.out.println("=                    Welcome, " + user.getName() + " (" + user.getRole() + ")");
            System.out.println("====================================================================");

            showRoleBasedMenu(user.getRole());

            System.out.println("=       0. Logout                                                 =");
            System.out.println("====================================================================");
            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();
            handleMenuChoice(choice,user);

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
                System.out.println("=       1. Create client                                      =");
                System.out.println("=       1. Create Account                                      =");
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
        switch (user.getRole()) {
            case ADMIN:
                handleAdminChoice(choice);
                break;
            case MANAGER:
                handleManagerChoice(choice);
                break;
            case TELLER:
                handleTellerChoice(choice,user);
                break;
            case AUDITOR:
                handleAuditorChoice(choice);
                break;
            default:
                System.out.println("Invalid role or choice.");
        }

    }

    private void handleAdminChoice(int choice){
    }

    private void handleManagerChoice(int choice){

    }

    private void handleTellerChoice(int choice, User user){
        switch(choice){
            case 1:
                clientHandler.createClient(user);
                break;
            case 2:
                System.out.println("create new account  ");
                break;
            default:
                System.out.println("invalid option , please try again. ");
                break;
        }
    }

    private void handleAuditorChoice(int choice){

    }
}

