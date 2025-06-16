package ar.uba.fi.ingsoft1.dynamicRules.relations;

import java.util.List;

public interface PredicateRelation {
    boolean evaluate(List<Boolean> predicateResults);
}
