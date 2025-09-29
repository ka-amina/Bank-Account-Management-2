import config.DatabaseConfig;

import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;
        do {
            System.out.println("====================================================================");
            System.out.println("=                         Bank Management Menu                     =");
            System.out.println("====================================================================");
            System.out.println("=       1. login                                                   =");
            System.out.println("=       2. exit                                                    =");
            System.out.println("====================================================================");
            System.out.println("=        enter your choice: ");
            choice=sc.nextInt();

        } while (choice != 0);
    }
}
