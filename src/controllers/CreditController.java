package controllers;

import entities.User;
import handlers.CreditHandler;

public class CreditController {
    private final CreditHandler creditHandler = new CreditHandler();

    public void requestCredit(User user) {
        creditHandler.requestCredit(user);
    }

    public void showCreditFollowUp() {
        creditHandler.showCreditFollowUp();
    }

    public void showPendingCredits() {
        creditHandler.showPendingCredits();
    }

    public void approveCredit(User manager) {
        creditHandler.approveCredit(manager);
    }

    public void rejectCredit() {
        creditHandler.rejectCredit();
    }

    public void showAllCredits() {
        creditHandler.showAllCredits();
    }
}