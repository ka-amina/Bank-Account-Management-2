package handlers;

import entities.Account;
import entities.Credit;
import entities.User;
import services.AccountService;
import services.CreditService;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class CreditHandler {
    private final Scanner sc = new Scanner(System.in);
    private final CreditService creditService = new CreditService();
    private final AccountService accountService = new AccountService();

    public void requestCredit(User user) {
        System.out.println("\n=== Credit Request ===");

        System.out.print("Account number: ");
        String accountNumber = sc.nextLine().trim();

        var accountOpt = accountService.findByNumber(accountNumber);
        if (!accountOpt.isPresent()) {
            System.out.println("✗ Account not found.");
            return;
        }

        Account account = accountOpt.get();
        if (!account.isActive()) {
            System.out.println("✗ Account is not active.");
            return;
        }

        System.out.print("Credit amount (MAD): ");
        BigDecimal amount;
        try {
            amount = new BigDecimal(sc.nextLine().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("✗ Amount must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid amount.");
            return;
        }

        System.out.print("Annual interest rate (%): ");
        BigDecimal interestRate;
        try {
            interestRate = new BigDecimal(sc.nextLine().trim());
            if (interestRate.compareTo(BigDecimal.ZERO) <= 0) {
                System.out.println("✗ Interest rate must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid interest rate.");
            return;
        }

        System.out.print("Duration (months): ");
        int duration;
        try {
            duration = Integer.parseInt(sc.nextLine().trim());
            if (duration <= 0) {
                System.out.println("✗ Duration must be positive.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("✗ Invalid duration.");
            return;
        }

        creditService.requestCredit(accountNumber, amount, interestRate, duration);
    }

    public void showCreditFollowUp() {
        System.out.print("\nEnter account number: ");
        String accountNumber = sc.nextLine().trim();

        List<Credit> credits = creditService.getCreditsByAccount(accountNumber);
        if (credits.isEmpty()) {
            System.out.println("No credits found for this account.");
            return;
        }

        System.out.println("\n" + "=".repeat(120));
        System.out.println("CREDIT HISTORY - Account: " + accountNumber);
        System.out.println("=".repeat(120));
        System.out.printf("%-5s | %-36s | %-12s | %-10s | %-10s | %-15s | %-20s\n",
                "No.", "Credit ID", "Amount", "Rate (%)", "Duration", "Status", "Created At");
        System.out.println("-".repeat(120));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < credits.size(); i++) {
            Credit c = credits.get(i);

            System.out.printf("%-5d | %-36s | %12.2f | %10.2f | %10d | %-15s | %-20s\n",
                    i + 1,
                    c.getId().toString(),
                    c.getAmount(),
                    c.getInterestRate(),
                    c.getDurationMonths(),
                    c.getStatus(),
                    c.getCreatedAt().format(formatter));
        }
        System.out.println("=".repeat(120));
    }

    public void showPendingCredits() {
        List<Credit> pendingCredits = creditService.getPendingCredits();

        if (pendingCredits.isEmpty()) {
            System.out.println("No pending credit requests.");
            return;
        }

        System.out.println("\n" + "=".repeat(120));
        System.out.println("PENDING CREDIT REQUESTS");
        System.out.println("=".repeat(120));
        System.out.printf("%-5s | %-36s | %-20s | %-12s | %-10s | %-10s | %-20s\n",
                "No.", "Credit ID", "Account", "Amount", "Rate (%)", "Duration", "Created At");
        System.out.println("-".repeat(120));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        for (int i = 0; i < pendingCredits.size(); i++) {
            Credit c = pendingCredits.get(i);
            System.out.printf("%-5d | %-36s | %-20s | %12.2f | %10.2f | %10d | %-20s\n",
                    i + 1,
                    c.getId().toString(),
                    c.getAccountId(),
                    c.getAmount(),
                    c.getInterestRate(),
                    c.getDurationMonths(),
                    c.getCreatedAt().format(formatter));
        }
        System.out.println("=".repeat(120));
    }

    public void approveCredit(User manager) {
        showPendingCredits();

        List<Credit> pendingCredits = creditService.getPendingCredits();
        if (pendingCredits.isEmpty()) {
            return;
        }

        System.out.print("\nEnter credit ID to approve: ");
        String creditIdStr = sc.nextLine().trim();

        UUID creditId;
        try {
            creditId = UUID.fromString(creditIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Invalid credit ID format.");
            return;
        }

        creditService.approveCredit(creditId);
    }

    public void rejectCredit() {
        showPendingCredits();

        List<Credit> pendingCredits = creditService.getPendingCredits();
        if (pendingCredits.isEmpty()) {
            return;
        }

        System.out.print("\nEnter credit ID to reject: ");
        String creditIdStr = sc.nextLine().trim();

        UUID creditId;
        try {
            creditId = UUID.fromString(creditIdStr);
        } catch (IllegalArgumentException e) {
            System.out.println("✗ Invalid credit ID format.");
            return;
        }

        creditService.rejectCredit(creditId);
    }

    public void showAllCredits() {
        List<Credit> credits = creditService.getAllCredits();

        if (credits.isEmpty()) {
            System.out.println("No credits found in the system.");
            return;
        }

        System.out.println("\n" + "=".repeat(120));
        System.out.println("ALL CREDITS");
        System.out.println("=".repeat(120));
        System.out.printf("%-5s | %-36s | %-20s | %-12s | %-10s | %-10s | %-15s\n",
                "No.", "Credit ID", "Account", "Amount", "Rate (%)", "Duration", "Status");
        System.out.println("-".repeat(120));

        for (int i = 0; i < credits.size(); i++) {
            Credit c = credits.get(i);
            System.out.printf("%-5d | %-36s | %-20s | %12.2f | %10.2f | %10d | %-15s\n",
                    i + 1,
                    c.getId().toString(),
                    c.getAccountId(),
                    c.getAmount(),
                    c.getInterestRate(),
                    c.getDurationMonths(),
                    c.getStatus());
        }
        System.out.println("=".repeat(120));
    }
}