package handlers;

import entities.FeeRule;
import enums.Modal;
import enums.TransactionType;
import services.FeeRuleService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

public class FeeRuleHandler {

    private final Scanner sc = new Scanner(System.in);
    private final FeeRuleService service = new FeeRuleService();

    public void listRules() {
        List<FeeRule> rules = service.getAll();
        if (rules.isEmpty()) {
            System.out.println("No fee rules configured.");
            return;
        }
        System.out.println("\n=== Fee Rules ===");
        rules.forEach(System.out::println);
    }

    public void addRule() {
        System.out.println("\n--- Add Fee Rule ---");
        System.out.print("Operation (TRANSFER_EXTERNAL, TRANSFER_INTERNAL, DEPOSIT, WITHDRAW, CREDIT): ");
        TransactionType type;
        try {
            type = TransactionType.valueOf(sc.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid type.");
            return;
        }

        System.out.print("Modal (FIX / PERCENT): ");
        Modal modal;
        try {
            modal = Modal.valueOf(sc.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid modal.");
            return;
        }

        System.out.print("Value (fixed amount or %): ");
        BigDecimal val;
        try {
            val = new BigDecimal(sc.nextLine().trim());
            if (val.compareTo(BigDecimal.ZERO) < 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            System.out.println("Positive numeric value required.");
            return;
        }

        FeeRule rule = new FeeRule(modal, val, type, true);
        boolean ok = service.addRule(rule);
        System.out.println(ok ? "Rule added." : "Failed to add rule.");
    }

    public void toggleRule() {
        listRules();
        System.out.print("Rule ID to toggle (ON/OFF): ");
        UUID id;
        try {
            id = UUID.fromString(sc.nextLine().trim());
        } catch (IllegalArgumentException ex) {
            System.out.println("Invalid UUID.");
            return;
        }
        service.getById(id).ifPresentOrElse(
                r -> {
                    boolean newState = !r.isActive();
                    boolean ok = service.toggle(id, newState);
                    System.out.println(ok ? "Rule " + (newState ? "activated" : "deactivated") : "Failed.");
                },
                () -> System.out.println("Rule not found."));
    }
}