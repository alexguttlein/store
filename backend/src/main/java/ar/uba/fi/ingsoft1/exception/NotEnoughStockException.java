package ar.uba.fi.ingsoft1.exception;

public class NotEnoughStockException extends RuntimeException {
    private String name;
    // private int amount;
    private int stock;

    public NotEnoughStockException(String msg, String productName, int stock) {
        super(msg);
        this.name = productName;
        // this.amount = amount;
        this.stock = stock;
    }

    public NotEnoughStockException(String product) {
        super("Not enough stock for product: " + product);
        this.name = product;
    }

    public String getProductName(){
        return this.name;
    }

    public int getCurrentlyAvailableStock(){
        return this.stock;
    }
}
