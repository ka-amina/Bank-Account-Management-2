package handlers;

import services.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

public class TransactionHandler {
    private final Scanner sc = new Scanner(System.in);
    private final AccountService accountService = new AccountService();

    public void deposit() {
        System.out.print("Account number to deposit into: ");
        String number = sc.nextLine().trim();

        System.out.print("Amount to deposit: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(sc.nextLine().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Amount must be a positive number.");
            return;
        }

        boolean ok = accountService.deposit(number, amount);
        if (ok) {
            System.out.println("Deposit successful.");
        } else {
            System.out.println("Deposit failed (account not found, not yours, or inactive).");
        }
    }

    public void withdraw() {
        System.out.print("Account number to withdraw from: ");
        String number = sc.nextLine().trim();

        System.out.print("Amount to withdraw: ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(sc.nextLine().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Amount must be a positive number.");
            return;
        }

        boolean ok = accountService.withdraw(number, amount);
        if (ok) {
            System.out.println("Withdrawal successful.");
        } else {
            System.out.println("Withdrawal failed (account not found, not yours, inactive, or insufficient balance).");
        }
    }
}