package ar.uba.fi.ingsoft1.cart;

import java.util.ArrayList;
import java.util.List;

import ar.uba.fi.ingsoft1.order.ItemDTO;



public record CartDTO(
        Long id,
        List<ItemDTO> products
        //User user

) {
    public CartDTO(Cart cart){
        this(cart.getId(),
        cart.getProducts().entrySet().stream()
            .map(entry -> new ItemDTO(entry.getKey(), entry.getValue()))
            .toList());
    }

    public static CartDTO create(Cart cart) {
        var items = new ArrayList<ItemDTO>();
        var products = cart.getProducts();
        products.forEach((prod, amount) -> {
            items.add(new ItemDTO(prod, amount));
        });

        return new CartDTO(cart.getId(), items);
    }
}