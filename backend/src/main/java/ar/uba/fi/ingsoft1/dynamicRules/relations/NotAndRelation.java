package ar.uba.fi.ingsoft1.dynamicRules.relations;

import java.util.List;

public class NotAndRelation implements PredicateRelation {
    @Override
    public boolean evaluate(List<Boolean> predicateResults) {
        return !predicateResults.stream().allMatch(Boolean::booleanValue);
    }
}
