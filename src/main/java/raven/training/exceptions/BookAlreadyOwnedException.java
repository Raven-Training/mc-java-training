package raven.training.exceptions;

public class BookAlreadyOwnedException extends RuntimeException {
    public BookAlreadyOwnedException() {
        super("El ID del libro ya se encuentra registrado");
    }

    public BookAlreadyOwnedException(String message) {
        super(message);
    }
}
