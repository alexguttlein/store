package ar.uba.fi.ingsoft1.dynamicRules.comparators;

public class GreaterThan implements ComparisonOperator {
    @Override
    public boolean compare(Object orderValue, Object ruleValue) {
        return Double.parseDouble(orderValue.toString()) > Double.parseDouble(ruleValue.toString());
    }
}
