package ar.uba.fi.ingsoft1.dynamicRules.objectClass;
import ar.uba.fi.ingsoft1.alternative.Alternative;
import ar.uba.fi.ingsoft1.cart.Cart;
import ar.uba.fi.ingsoft1.order.Item;
import ar.uba.fi.ingsoft1.product.Product;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.isNull;

public class AlternativeEvaluator implements ObjectClassEvaluator {

    /**
     * Metodo que obtiene los valores de las alternativas/categorias cuyo nombre coniciden
     * con el key del parametro y los devuelve en una lista
     * @param cart carrito
     * @param key clave
     * @return lista de alternativas
     */
    @Override
    public Object getValue(Cart cart, String key) {
        List<String> alternatives = new ArrayList<>();
        for (Item item : cart) {
            var product = item.product();
            var amount = item.amount();
            Alternative alternative = product.getAlternative();
            if (alternative.getAlternativeName().equals(key)){
                for (int i = 0; i < amount; i++) {
                    alternatives.add(alternative.getAlternativeName());
                }
            }
        }
        return alternatives;
    }
}
