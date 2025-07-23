package raven.training.exceptions;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("El libro no fue encontrado.");
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}

