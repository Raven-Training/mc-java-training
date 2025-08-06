package raven.training.exceptions;

/**
 * Excepción lanzada cuando se intenta actualizar un libro y el ID no coincide
 * que ya lo tiene registrado.
 *
 * Esta excepción extiende {@link RuntimeException}, por lo que es no verificada (unchecked).
 * Puede utilizarse para controlar conflictos de duplicación lógica en el dominio de negocio.
 */
public class BookIdMismatchException extends RuntimeException {
    public BookIdMismatchException() {
        super("El ID del libro no coincide con el ID de la URL.");
    }

    public BookIdMismatchException(String message) {
        super(message);
    }
}

