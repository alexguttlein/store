package ar.uba.fi.ingsoft1.dynamicRules.objectClass;

import ar.uba.fi.ingsoft1.attribute.Attribute;
import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.product.Product;

import java.util.ArrayList;
import java.util.List;

public class StringAttributeEvaluator implements ObjectClassEvaluator {

    /**
     * Metodo que obtiene los valores de los atributos cuyo nombre coniciden con el key del parametro
     * y los devuelve en una lista
     * @param cart carrito
     * @param key clave
     * @return lista de atributos
     */
    @Override
    public Object getValue(Cart cart, String key) {
        List<String> values = new ArrayList<>();
        for (Product product : cart.getProducts().keySet()) {
            for (Attribute attribute : product.getAttributes()) {
                if (attribute.getAttributeName().equalsIgnoreCase(key)) {
                    values.add(attribute.getValue());
                }
            }
        }
        return values;
    }
}
