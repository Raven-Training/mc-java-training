package raven.training.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Puedes agregar más handlers para otras excepciones aquí

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleUnsupportedOp(BookNotFoundException ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", 400);
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("error", "Book not found");
        errorBody.put("message", ex.getMessage());

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookIdMismatchException.class)
    public ResponseEntity<Object> handleBookIdMismatch(BookIdMismatchException ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", 400);
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("error", "Book ID Mismatch");
        errorBody.put("message", ex.getMessage());

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BookAlreadyOwnedException.class)
    public ResponseEntity<Object> handleBookIdMismatch(BookAlreadyOwnedException ex) {
        Map<String, Object> errorBody = new HashMap<>();
        errorBody.put("status", 400);
        errorBody.put("timestamp", LocalDateTime.now());
        errorBody.put("error", "Book ID Mismatch");
        errorBody.put("message", ex.getMessage());

        return new ResponseEntity<>(errorBody, HttpStatus.BAD_REQUEST);
    }

}
