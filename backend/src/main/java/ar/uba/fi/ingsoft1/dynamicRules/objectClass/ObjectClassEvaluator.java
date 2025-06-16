package ar.uba.fi.ingsoft1.dynamicRules.objectClass;

import ar.uba.fi.ingsoft1.cart.Cart;

public interface ObjectClassEvaluator {
    Object getValue(Cart cart, String key);
}
