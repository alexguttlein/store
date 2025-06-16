package ar.uba.fi.ingsoft1.dynamicRules.objectClass;

import ar.uba.fi.ingsoft1.attribute.Attribute;
import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperator;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperatorFactory;
import ar.uba.fi.ingsoft1.dynamicRules.operators.MultiplicationOperator;
import ar.uba.fi.ingsoft1.order.Item;
import ar.uba.fi.ingsoft1.product.Product;

public class NumericAttributeEvaluator implements ObjectClassEvaluator {
    private final ArithmeticOperator arithmeticOperator;

    public NumericAttributeEvaluator(ArithmeticOperator arithmeticOperator) {
        this.arithmeticOperator = arithmeticOperator;
    }

    /**
     * Metodo que obtiene los valores de los atributos cuyo nombre coniciden con el key del parametro
     * y realizan la operacion aritmetica correspondiente
     * @param cart carrito
     * @param key clave
     * @return valor de objeto
     */
    @Override
    public Object getValue(Cart cart, String key) {
        double totalValue = arithmeticOperator instanceof MultiplicationOperator ? 1 : 0;
        for (Item item : cart) {
            var product = item.product();
            var amount = item.amount();
            for (Attribute attribute : product.getAttributes()) {
                if (attribute.getAttributeName().equals(key)) {
                    // Se multiplica el valor del atributo por la cantidad de productos iguales
                    // y luego se realiza la operacion solicitada deesde el archivo
                    double totalValueProd = ArithmeticOperatorFactory.create("*").operate(Double.parseDouble(attribute.getValue()), amount);
                    totalValue = arithmeticOperator.operate(totalValueProd, totalValue);
                }
            }
        }
        return totalValue;
    }
}
