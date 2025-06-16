package ar.uba.fi.ingsoft1.dynamicRules;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ar.uba.fi.ingsoft1.cart.Cart;

public class RuleEngine {
    private List<Rule> rules;

    public RuleEngine(List<Rule> rules) {
        this.rules = rules;
    }

    public RuleEngine(){
        this.rules = new LinkedList<>();
    }

    /**
     * Metodo que valida si se cumplen todas las reglas compuestas
     * @param cart pedido sobre el que se evaluaran las reglas
     * @return true si todas las reglas son cumplidas
     */
    public List<String> validateOrder(Cart cart) {
        List<String> errorMessages = new ArrayList<>();
        for (Rule rule : rules) {
            if (!rule.interpret(cart)) {
                errorMessages.add(rule.getMessageError());
            }
        }
        return errorMessages;
    }

    public void addRules(List<Rule> rules){
        this.rules.addAll(rules);
    }

    public List<Rule> getRules(){return this.rules;}
}
