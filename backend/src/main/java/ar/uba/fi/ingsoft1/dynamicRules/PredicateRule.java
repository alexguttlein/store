package ar.uba.fi.ingsoft1.dynamicRules;

import static java.util.Objects.isNull;

import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.dynamicRules.comparators.ComparisonOperator;
import ar.uba.fi.ingsoft1.dynamicRules.comparators.ComparisonOperatorFactory;
import ar.uba.fi.ingsoft1.dynamicRules.objectClass.ObjectClassEvaluator;
import ar.uba.fi.ingsoft1.dynamicRules.objectClass.ObjectClassEvaluatorFactory;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperator;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperatorFactory;

public class PredicateRule implements Rule {

    private String key;
    private Object value;
    private ArithmeticOperator arithmeticOperator;
    private ComparisonOperator comparisonOperator;
    private ObjectClassEvaluator evaluator;

    public PredicateRule(String key, Object value, String arithmeticOperator, String comparisonOperator, String objectClass) {
        this.key = key;
        this.value = value;
        this.arithmeticOperator = ArithmeticOperatorFactory.create(arithmeticOperator);
        this.comparisonOperator = ComparisonOperatorFactory.create(comparisonOperator);
        this.evaluator = ObjectClassEvaluatorFactory.create(objectClass, this.arithmeticOperator);
    }

    /**
     * Metodo que obtiene el valor del Cart que va a comparar
     * Y luego se lo envia al metodo que compara
     * @param cart carrito
     * @return boolean
     */
    @Override
    public boolean interpret(Cart cart) {
        Object orderValue = evaluator.getValue(cart, key);
        return !isNull(orderValue) && comparisonOperator.compare(orderValue, value);
    }

    @Override
    public String getMessageError() {
        return null;
    }

}
