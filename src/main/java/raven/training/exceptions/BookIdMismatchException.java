package raven.training.exceptions;

public class BookIdMismatchException extends RuntimeException {
    public BookIdMismatchException() {
        super("El ID del libro no coincide con el ID de la URL.");
    }

    public BookIdMismatchException(String message) {
        super(message);
    }
}

