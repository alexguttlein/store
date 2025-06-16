package ar.uba.fi.ingsoft1.dynamicRules.relations;

import java.util.List;

public class OrRelation implements PredicateRelation {
    @Override
    public boolean evaluate(List<Boolean> predicateResults) {
        return predicateResults.stream().anyMatch(Boolean::booleanValue);
    }
}
