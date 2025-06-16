package ar.uba.fi.ingsoft1.dynamicRules.objectClass;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperator;
public class ObjectClassEvaluatorFactory {
    public static ObjectClassEvaluator create(String objectClass, ArithmeticOperator arithmeticOperator) {
        switch (objectClass.toLowerCase()) {
            case "stringattribute", "string attribute" -> {
                return new StringAttributeEvaluator();
            }
            case "numericattribute", "numeric attribute" -> {
                return new NumericAttributeEvaluator(arithmeticOperator);
            }
            case "alternative" -> {
                return new AlternativeEvaluator();
            }
            case "numericalternative" -> {
                return new NumericAlternativeEvaluator(arithmeticOperator);
            }
            case "cart" -> {
                return new CartEvaluator(arithmeticOperator);
            }
            default -> throw new IllegalArgumentException("Invalid objectClass: " + objectClass);
        }
    }
}
