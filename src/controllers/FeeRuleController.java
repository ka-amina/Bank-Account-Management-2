package controllers;

import handlers.FeeRuleHandler;

public class FeeRuleController {
    private final FeeRuleHandler handler = new FeeRuleHandler();

    public void listRules() { handler.listRules(); }
    public void addRule()   { handler.addRule(); }
    public void toggleRule(){ handler.toggleRule(); }
}