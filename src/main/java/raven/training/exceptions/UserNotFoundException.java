package raven.training.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("El usuario no fue encontrado.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
