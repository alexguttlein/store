package ar.uba.fi.ingsoft1.exception;

public class ErrorAtSendingEmailException extends RuntimeException {
    public ErrorAtSendingEmailException(String message) { super(message); }
}
