package raven.training.exceptions;

/**
 * Excepción lanzada cuando se intenta asociar un libro a un usuario
 * que ya lo tiene registrado.
 *
 * Esta excepción extiende {@link RuntimeException}, por lo que es no verificada (unchecked).
 * Puede utilizarse para controlar conflictos de duplicación lógica en el dominio de negocio.
 */
public class BookAlreadyOwnedException extends RuntimeException {
    public BookAlreadyOwnedException() {
        super("El ID del libro ya se encuentra registrado");
    }

    public BookAlreadyOwnedException(String message) {
        super(message);
    }
}
