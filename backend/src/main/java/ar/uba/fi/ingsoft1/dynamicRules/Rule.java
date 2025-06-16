package ar.uba.fi.ingsoft1.dynamicRules;

import ar.uba.fi.ingsoft1.cart.Cart;

public interface Rule {
    boolean interpret(Cart cart);
    String getMessageError();
}
