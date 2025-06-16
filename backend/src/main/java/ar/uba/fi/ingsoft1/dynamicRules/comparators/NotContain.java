package ar.uba.fi.ingsoft1.dynamicRules.comparators;

public class NotContain implements ComparisonOperator {
    @Override
    public boolean compare(Object orderValue, Object ruleValue) {
        return !orderValue.toString().contains(ruleValue.toString());
    }
}
