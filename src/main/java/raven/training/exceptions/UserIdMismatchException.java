package raven.training.exceptions;

public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {
        super("El ID del libro no coincide con el ID de la URL.");
    }

    public UserIdMismatchException(String message) {
        super(message);
    }
}
