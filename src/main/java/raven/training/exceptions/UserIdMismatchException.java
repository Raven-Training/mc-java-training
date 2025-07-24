package raven.training.exceptions;
/**
 * Excepción lanzada cuando se intenta actualizar un usuario y el ID no coincide
 * que ya lo tiene registrado.
 *
 * Esta excepción extiende {@link RuntimeException}, por lo que es no verificada (unchecked).
 * Puede utilizarse para controlar conflictos de duplicación lógica en el dominio de negocio.
 */
public class UserIdMismatchException extends RuntimeException {

    public UserIdMismatchException() {
        super("El ID del libro no coincide con el ID de la URL.");
    }

    public UserIdMismatchException(String message) {
        super(message);
    }
}
