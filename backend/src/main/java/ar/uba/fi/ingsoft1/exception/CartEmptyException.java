package ar.uba.fi.ingsoft1.exception;

public class CartEmptyException extends RuntimeException {
    public CartEmptyException(String message) { super(message); }

    public CartEmptyException() {
        super("Cart is empty");
    }
}



