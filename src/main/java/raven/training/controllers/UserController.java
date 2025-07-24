package raven.training.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raven.training.exceptions.BookNotFoundException;
import raven.training.exceptions.UserNotFoundException;
import raven.training.exceptions.UserIdMismatchException;
import raven.training.models.Book;
import raven.training.models.User;
import raven.training.repositories.BookRepository;
import raven.training.repositories.UserRepository;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con los usuarios del sistema.
 * Proporciona endpoints para consultar, crear, actualizar y eliminar usuarios,
 * así como para asociar o desasociar libros a un usuario.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario a buscar.
     * @return El usuario correspondiente si existe.
     * @throws UserNotFoundException si no se encuentra el usuario.
     */
    @GetMapping("/{id}")
    public User findOne(@PathVariable Long id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
    }

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param userName Nombre del usuario a buscar.
     * @return El usuario correspondiente, o null si no existe.
     */
    @GetMapping("/username/{userName}")
    public User findByUserName(@PathVariable String userName) {

        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + userName);
        }
        return user;
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param user Objeto usuario a guardar.
     * @return El usuario creado con su ID asignado.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @throws UserNotFoundException si el usuario no existe.
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        userRepository.deleteById(id);
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param user Objeto usuario con los nuevos datos.
     * @param id   ID del usuario que se desea actualizar.
     * @return El usuario actualizado.
     * @throws UserIdMismatchException si el ID del path no coincide con el del objeto usuario.
     * @throws UserNotFoundException   si el usuario no existe.
     */
    @PutMapping("/{id}")
    public User updateUser(@RequestBody User user, @PathVariable Long id) {
        if (user.getId() != id) {
            throw new UserIdMismatchException();
        }
        userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
        return userRepository.save(user);
    }

    /**
     * Asocia un libro existente a un usuario.
     *
     * @param userId ID del usuario.
     * @param bookId ID del libro a asociar.
     * @return El usuario actualizado con el libro añadido.
     * @throws UserNotFoundException si el usuario no existe.
     * @throws BookNotFoundException si el libro no existe.
     */
    @PutMapping("/{userId}/books/{bookId}")
    public User addBookToUser(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado"));
        user.addBook(book);
        return userRepository.save(user);
    }

    /**
     * Desasocia un libro de un usuario.
     *
     * @param userId ID del usuario.
     * @param bookId ID del libro a eliminar de su lista.
     * @return El usuario actualizado sin el libro.
     * @throws UserNotFoundException si el usuario no existe.
     * @throws BookNotFoundException si el libro no existe.
     */
    @DeleteMapping("/{userId}/books/{bookId}")
    public User removeBookFromUser(@PathVariable Long userId, @PathVariable Long bookId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Libro no encontrado"));
        user.removeBook(book);
        return userRepository.save(user);
    }
}
