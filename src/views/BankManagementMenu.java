package views;

import entities.User;
import enums.UserRole;

import java.util.Scanner;

public class BankManagementMenu {
    private Scanner scanner = new Scanner(System.in);

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
            choice = scanner.nextInt();
            scanner.nextLine();

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
                System.out.println("logged in as an Teller");
                break;
            case AUDITOR:
                System.out.println("logged in as an Auditor");
                break;
        }
    }
}

