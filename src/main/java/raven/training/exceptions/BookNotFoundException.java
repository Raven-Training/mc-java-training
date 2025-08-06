package raven.training.exceptions;
/**
 * Excepción lanzada cuando se intenta buscar un libro y no se encuentra
 * que ya lo tiene registrado.
 *
 * Esta excepción extiende {@link RuntimeException}, por lo que es no verificada (unchecked).
 * Puede utilizarse para controlar conflictos de duplicación lógica en el dominio de negocio.
 */
public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException() {
        super("El libro no fue encontrado.");
    }

    public BookNotFoundException(String message) {
        super(message);
    }
}

