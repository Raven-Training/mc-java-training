package raven.training.exceptions;
/**
 * Excepción lanzada cuando se intenta buscar un usuario y no se encuentra
 * que ya lo tiene registrado.
 *
 * Esta excepción extiende {@link RuntimeException}, por lo que es no verificada (unchecked).
 * Puede utilizarse para controlar conflictos de duplicación lógica en el dominio de negocio.
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("El usuario no fue encontrado.");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
