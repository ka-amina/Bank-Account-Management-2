import controllers.LoginController;
import entities.User;
import views.BankManagementMenu;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LoginController loginView = new LoginController();
        BankManagementMenu bankMenu = new BankManagementMenu();

        int choice;
        do {
            System.out.println("====================================================================");
            System.out.println("=                         Bank Management Menu                     =");
            System.out.println("====================================================================");
            System.out.println("=       1. login                                                   =");
            System.out.println("=       2. exit                                                    =");
            System.out.println("====================================================================");
            System.out.println("=        enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    User loggedInUser = loginView.showLoginMenu();
                    if (loggedInUser != null) {
                        bankMenu.showMenu(loggedInUser);
                    }
                    break;
                case 2:
                    System.out.println("Exiting the system. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }

        } while (choice != 0);
    }
}
