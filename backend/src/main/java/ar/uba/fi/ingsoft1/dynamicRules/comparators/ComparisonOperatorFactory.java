package ar.uba.fi.ingsoft1.dynamicRules.comparators;

public class ComparisonOperatorFactory {
    public static ComparisonOperator create(String operator) {
        if (operator == null || operator.trim().isEmpty()) {
            throw new IllegalArgumentException("Comparison operator cannot be null or empty");
        }

        switch (operator.toLowerCase()) {
            case "==":
                return new Equal();
            case "!=":
                return new NotEqual();
            case ">":
                return new GreaterThan();
            case "<":
                return new LessThan();
            case ">=":
                return new GreaterEqual();
            case "<=":
                return new LessEqual();
            case "contains":
                return new Contains();
            case "not contain":
            case "notcontain":
                return new NotContain();
            default:
                throw new IllegalArgumentException("Invalid comparison operator: " + operator);
        }
    }
}
