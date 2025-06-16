package ar.uba.fi.ingsoft1.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public record OrderDTO(
    Long id,
    List<ItemDTO> items,
    Long  idOwner,
    OrderState state,
    LocalDateTime orderDate

){

    public OrderDTO(Order order) {
        this(order.getId(),
        order.getItems().stream()
             .map(ItemDTO::new)
             .toList(), order.getOrderOwner().getId(), order.getState(), order.getOrderDate());
    }

    public static OrderDTO create(Order order){
        List<ItemDTO> list = new ArrayList<>(); 
        Iterable<Item> orderItems = order.getItems();
        for (Item item : orderItems) {
            list.add(new ItemDTO(item));

        }
        
        return new OrderDTO(order.getId(), list, order.getOrderOwner().getId(), order.getState(), order.getOrderDate());
    }

}