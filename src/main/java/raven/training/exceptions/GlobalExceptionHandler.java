package raven.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase encargada de manejar globalmente las excepciones lanzadas por los controladores REST.
 * Utiliza {@link ControllerAdvice} para interceptar excepciones específicas y devolver respuestas personalizadas.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Construye una respuesta de error estandarizada con información útil para el cliente.
     *
     * @param status Código de estado HTTP.
     * @param error  Descripción del tipo de error.
     * @param ex     La excepción original lanzada.
     * @return Una respuesta HTTP con un cuerpo detallado del error.
     */
    private ResponseEntity<Object> buildErrorResponse(HttpStatus status, String error, Exception ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", status.value());
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("error", error);
        errorBody.put("message", ex.getMessage());
        return new ResponseEntity<>(errorBody, status);
    }

    /**
     * Maneja excepciones cuando no se encuentra un libro.
     *
     * @param ex Excepción {@link BookNotFoundException}.
     * @return Respuesta con estado 404 y mensaje personalizado.
     */
    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFound(BookNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Book not found", ex);
    }

    /**
     * Maneja excepciones cuando el ID del libro no coincide con el esperado.
     *
     * @param ex Excepción {@link BookIdMismatchException}.
     * @return Respuesta con estado 400 y mensaje de error.
     */
    @ExceptionHandler(BookIdMismatchException.class)
    public ResponseEntity<Object> handleBookIdMismatch(BookIdMismatchException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Book ID mismatch", ex);
    }

    /**
     * Maneja excepciones cuando no se encuentra un usuario.
     *
     * @param ex Excepción {@link UserNotFoundException}.
     * @return Respuesta con estado 404 y mensaje de error.
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFound(UserNotFoundException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "User not found", ex);
    }

    /**
     * Maneja excepciones cuando el ID del usuario no coincide con el esperado.
     *
     * @param ex Excepción {@link UserIdMismatchException}.
     * @return Respuesta con estado 400 y mensaje de error.
     */
    @ExceptionHandler(UserIdMismatchException.class)
    public ResponseEntity<Object> handleUserIdMismatch(UserIdMismatchException ex) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "User ID mismatch", ex);
    }

    /**
     * Maneja excepciones cuando un usuario ya tiene registrado un libro.
     *
     * @param ex Excepción {@link BookAlreadyOwnedException}.
     * @return Respuesta con estado 409 (conflicto) y mensaje de error.
     */
    @ExceptionHandler(BookAlreadyOwnedException.class)
    public ResponseEntity<Object> handleBookAlreadyOwned(BookAlreadyOwnedException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, "Book already owned by user", ex);
    }
}
