package ar.uba.fi.ingsoft1.dynamicRules.comparators;

public class Equal implements ComparisonOperator {
    @Override
    public boolean compare(Object orderValue, Object ruleValue) {
        return orderValue.equals(ruleValue);
    }
}
