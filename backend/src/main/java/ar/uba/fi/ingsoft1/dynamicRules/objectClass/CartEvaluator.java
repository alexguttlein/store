package ar.uba.fi.ingsoft1.dynamicRules.objectClass;

import ar.uba.fi.ingsoft1.attribute.Attribute;
import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperator;
import ar.uba.fi.ingsoft1.dynamicRules.operators.ArithmeticOperatorFactory;
import ar.uba.fi.ingsoft1.dynamicRules.operators.MultiplicationOperator;
import ar.uba.fi.ingsoft1.order.Item;

import java.util.HashMap;
import java.util.Map;

public class CartEvaluator implements ObjectClassEvaluator {
    private final ArithmeticOperator arithmeticOperator;

    public CartEvaluator(ArithmeticOperator arithmeticOperator) {
        this.arithmeticOperator = arithmeticOperator;
    }
    @Override
    public Object getValue(Cart cart, String key) {
        switch (key.toLowerCase()) {
            case "quantity" -> {
                double totalValue = arithmeticOperator instanceof MultiplicationOperator ? 1 : 0;
                // Devuelve el total de productos en el carrito
                for (Item item : cart) {
                    var product = item.product();
                    var amount = item.amount();
                    totalValue = arithmeticOperator.operate(totalValue, amount);
                }
                    return totalValue;
            }
            case "maxproductquantity" -> {
                // Devuelve el máximo número de un mismo producto en el carrito
                Map<Long, Integer> productCounts = new HashMap<>();
                for (Item item : cart) {
                    var product = item.product();
                    var amount = item.amount();
                    Long productId = product.getId();
                    productCounts.put(productId, productCounts.getOrDefault(productId, 0) + amount);
                }
                return productCounts.values().stream().max(Integer::compareTo).orElse(0);
            }
            default -> throw new IllegalArgumentException("Invalid key for cart evaluation: " + key);
        }
    }
}
