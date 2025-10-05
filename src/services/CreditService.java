package services;

import entities.Account;
import entities.Credit;
import enums.CreditStatus;
import repositories.AccountRepositoryImpl;
import repositories.AccoutRepository;
import repositories.CreditRepository;
import repositories.CreditRepositoryImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class CreditService {
    private final CreditRepository creditRepository = new CreditRepositoryImpl();
//    private final AccountService accountService = new AccountService();
    private final AccoutRepository accoutRepository = new AccountRepositoryImpl();
    public Credit requestCredit(String accountNumber, BigDecimal amount,
                                BigDecimal interestRate, int durationMonths) {

        Credit credit = new Credit(accountNumber, amount, interestRate, durationMonths);
        credit.setStatus(CreditStatus.PENDING);

        if (creditRepository.createCredit(credit)) {
            System.out.println("✓ Credit request submitted successfully!");
            System.out.println("  Account: " + accountNumber);
            System.out.println("  Amount: " + amount + " MAD");
            System.out.println("  Interest rate: " + interestRate + "%");
            System.out.println("  Duration: " + durationMonths + " months");
            System.out.println("  Status: PENDING - Awaiting manager approval");
            return credit;
        }

        return null;
    }

    public boolean approveCredit(UUID creditId) {
        Optional<Credit> creditOpt = creditRepository.findById(creditId);
        if (!creditOpt.isPresent()) {
            System.out.println("✗ Credit not found.");
            return false;
        }

        Credit credit = creditOpt.get();
        if (credit.getStatus() != CreditStatus.PENDING) {
            System.out.println("✗ Credit is not in pending status.");
            return false;
        }

        Optional<Account> accountOpt = accoutRepository.findByNumber(credit.getAccountId());
        if (!accountOpt.isPresent() || !accountOpt.get().isActive()) {
            System.out.println("✗ Account not found or inactive.");
            return false;
        }

        boolean deposited = accoutRepository.deposit(credit.getAccountId(), credit.getAmount());
        if (!deposited) {
            System.out.println("✗ Failed to deposit credit amount to account.");
            return false;
        }

        boolean updated = creditRepository.updateStatus(creditId, CreditStatus.ACTIVE);
        if (updated) {
            System.out.println("✓ Credit approved successfully!");
            System.out.println("  Credit ID: " + creditId);
            System.out.println("  Amount: " + credit.getAmount() + " MAD");
            System.out.println("  Status: ACTIVE");
        }
        return updated;
    }

    public boolean rejectCredit(UUID creditId) {
        Optional<Credit> creditOpt = creditRepository.findById(creditId);
        if (!creditOpt.isPresent()) {
            System.out.println("✗ Credit not found.");
            return false;
        }

        Credit credit = creditOpt.get();
        if (credit.getStatus() != CreditStatus.PENDING) {
            System.out.println("✗ Credit is not in pending status.");
            return false;
        }

        boolean updated = creditRepository.updateStatus(creditId, CreditStatus.REJECTED);
        if (updated) {
            System.out.println("✓ Credit rejected successfully!");
        }
        return updated;
    }

    public boolean closeCredit(UUID creditId) {
        return creditRepository.updateStatus(creditId, CreditStatus.CLOSED);
    }

    public boolean markAsLate(UUID creditId) {
        return creditRepository.updateStatus(creditId, CreditStatus.LATE);
    }

    public List<Credit> getCreditsByAccount(String accountNumber) {
        return creditRepository.findByAccountId(accountNumber);
    }

    public List<Credit> getAllCredits() {
        return creditRepository.findAll();
    }

    public List<Credit> getPendingCredits() {
        return creditRepository.findByStatus(CreditStatus.PENDING);
    }

    public List<Credit> getActiveCredits() {
        return creditRepository.findByStatus(CreditStatus.ACTIVE);
    }

    public Optional<Credit> findById(UUID creditId) {
        return creditRepository.findById(creditId);
    }
}