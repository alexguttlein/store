package ar.uba.fi.ingsoft1.dynamicRules;

import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.dynamicRules.relations.PredicateRelation;
import ar.uba.fi.ingsoft1.dynamicRules.relations.RelationFactory;

import java.util.List;
import java.util.stream.Collectors;

public class CompositeRule implements Rule {
    private List<Rule> rules;
    private PredicateRelation relation;
    private String scope;
    private String messageError;

    public CompositeRule(List<Rule> rules, String predicatesRelation, String scope, String messageError) {
        this.rules = rules;
        this.relation = RelationFactory.create(predicatesRelation);
        this.scope = scope;
        this.messageError = messageError;
    }

    @Override
    public boolean interpret(Cart cart) {
        // se evalua cada regla individual
        List<Boolean> predicateResults = rules.stream()
                .map(rule -> rule.interpret(cart))
                .collect(Collectors.toList());

        // se evalua la relacion entre las reglas
        return relation.evaluate(predicateResults);
    }

    @Override
    public String getMessageError() {
        return this.messageError;
    }
}
