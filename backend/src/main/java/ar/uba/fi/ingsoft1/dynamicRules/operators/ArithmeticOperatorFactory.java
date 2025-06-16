package ar.uba.fi.ingsoft1.dynamicRules.operators;

public class ArithmeticOperatorFactory {
    public static ArithmeticOperator create(String operator) {

        if (operator == null || operator.trim().isEmpty()) {
            return new NoOperation();
        }

        return switch (operator) {
            case "+" -> new AdditionOperator();
            case "*" -> new MultiplicationOperator();
            case "-" -> new SubtractionOperator();
            default -> throw new IllegalArgumentException("Unknown arithmetic operator: " + operator);
        };
    }
}
