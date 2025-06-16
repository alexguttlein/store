package ar.uba.fi.ingsoft1.dynamicRules.comparators;

public class NotEqual implements ComparisonOperator {
    @Override
    public boolean compare(Object orderValue, Object ruleValue) {
        return !orderValue.equals(ruleValue);
    }
}
