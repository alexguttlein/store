package ar.uba.fi.ingsoft1.exception;

public class InvalidCartContentsException extends RuntimeException {
    public InvalidCartContentsException(String message) { super(message); }

    public InvalidCartContentsException() {
        super("Invalid cart contents");
    }
}



