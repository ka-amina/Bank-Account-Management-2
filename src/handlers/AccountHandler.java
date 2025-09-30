package handlers;

import entities.Account;
import entities.User;
import enums.AccountType;
import services.AccountService;
import services.ClientService;
import utils.AccountNumberGenerator;

import java.util.Scanner;

public class AccountHandler {
    private final Scanner sc = new Scanner(System.in);
    private final AccountService accountService = new AccountService();
    private final ClientService clientService = new ClientService();

    public Account createAccount(User teller) {
        System.out.print("Client CIN: ");
        String cin = sc.nextLine().trim();

        var clientId = clientService.findClientIdByCin(cin);
        if (clientId == null) {
            System.out.println("No client found with CIN: " + cin);
            return null;
        }

        System.out.println("Choose account type:");
        System.out.println("1 - CURRENT");
        System.out.println("2 - SAVINGS");
        System.out.println("3 - CREDIT");
        System.out.print("Your choice: ");
        AccountType type;
        switch (sc.nextLine().trim()) {
            case "2":
                type = AccountType.SAVINGS;
                break;
            case "3":
                type = AccountType.CREDIT;
                break;
            default:
                type = AccountType.CURRENT;
                break; // 1 or anything else
        }

        String accountNumber = AccountNumberGenerator.next(); // 10-digit String

        Account account = accountService.createAccount(accountNumber, type, clientId, teller.getId());
        if (account != null) {
            System.out.println("Account created successfully. Number: " + account.getAccountNumber());
        } else {
            System.out.println("Failed to create account.");
        }
        return account;
    }
}
