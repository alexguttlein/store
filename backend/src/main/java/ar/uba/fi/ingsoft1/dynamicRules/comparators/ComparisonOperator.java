package ar.uba.fi.ingsoft1.dynamicRules.comparators;

public interface ComparisonOperator {
    boolean compare(Object orderValue, Object ruleValue);
}
