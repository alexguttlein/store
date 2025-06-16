package ar.uba.fi.ingsoft1.dynamicRules.comparators;

public class Contains implements ComparisonOperator {
    @Override
    public boolean compare(Object orderValue, Object ruleValue) {
        return orderValue.toString().contains(ruleValue.toString());
    }
}
