package ca.gbc.bookingservice.exception;

public class FallbackException extends RuntimeException {
    public FallbackException(String message) {
        super(message);
    }
}
