package ar.uba.fi.ingsoft1.dynamicRules.objectClass;

import ar.uba.fi.ingsoft1.alternative.Alternative;
import ar.uba.fi.ingsoft1.attribute.Attribute;
import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperator;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperatorFactory;
import ar.uba.fi.ingsoft1.dynamicRules.operators.MultiplicationOperator;
import ar.uba.fi.ingsoft1.order.Item;

import java.math.BigDecimal;

public class NumericAlternativeEvaluator implements ObjectClassEvaluator {
    private final ArithmeticOperator arithmeticOperator;
    public NumericAlternativeEvaluator(ArithmeticOperator arithmeticOperator){
        this.arithmeticOperator = arithmeticOperator;
    }

    @Override
    public Object getValue(Cart cart, String key) {
        double totalValue = arithmeticOperator instanceof MultiplicationOperator ? 1 : 0;
        for (Item item : cart) {
            var product = item.product();
            var amount = item.amount();
            Alternative alternative = product.getAlternative();
            if (alternative.getAlternativeName().equals(key)) {
                totalValue = arithmeticOperator.operate(ArithmeticOperatorFactory.create("*")
                                .operate(Double.parseDouble(String.valueOf(BigDecimal.ONE)), amount), totalValue);
            }
        }
        return totalValue;
    }
}
