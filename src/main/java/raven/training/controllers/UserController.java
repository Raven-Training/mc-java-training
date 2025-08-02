package raven.training.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raven.training.exceptions.BookNotFoundException;
import raven.training.exceptions.UserNotFoundException;
import raven.training.exceptions.UserIdMismatchException;
import raven.training.models.Book;
import raven.training.models.User;
import raven.training.repositories.BookRepository;
import raven.training.repositories.UserRepository;
import raven.training.services.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

/**
 * Controlador REST para gestionar operaciones relacionadas con los usuarios del sistema.
 * Proporciona endpoints para consultar, crear, actualizar y eliminar usuarios,
 * así como para asociar o desasociar libros a un usuario.
 */
@Tag(name = "Users", description = "Operaciones relacionadas con los usuarios del sistema")
@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookRepository bookRepository;

    /**
     * Busca todos los usuarios
     *
     * @return El usuario correspondiente si existe.
     */
    @Operation(summary = "Buscar todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuarios encontrados"),
            @ApiResponse(responseCode = "404", description = "No hay usuarios creados")
    })
    @GetMapping
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario a buscar.
     * @return El usuario correspondiente si existe.
     * @throws UserNotFoundException si no se encuentra el usuario.
     */
    @Operation(summary = "Buscar usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
    @Operation(summary = "Buscar usuario por nombre de usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(responseCode = "404", description = "User not found with username")
    })
    @GetMapping("/username/{userName}")
    public User findByUserName(@PathVariable String userName) {

        User user = userRepository.findByUserName(userName);
        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + userName);
        }
        return user;
    }

    @Operation(summary = "Retornar usuario logueado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informacion del usuario logueado")
    })
    @GetMapping(value = "/username")
    public User currentUserName(Principal principal) {
        String name = principal.getName();

        return userRepository.findByUserName(name);
    }

    @Operation(summary = "Retornar usuario que haga match")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Informacion del usuario")
    })
    @GetMapping(value = "/findBetween/{startDate}/{endDate}/{namePart}")
    public List<User> findByBirthDateBetweenAndNameContainingIgnoreCase(@PathVariable LocalDate startDate, @PathVariable LocalDate endDate,@PathVariable String namePart) {
        return userRepository.findByBirthDateBetweenAndNameContainingIgnoreCase(startDate,endDate,namePart);
    }

    @GetMapping("/search")
    public List<User> searchUsers(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String namePart
    ) {
        return userRepository.searchUsers(
                (startDate == null || startDate.isBlank()) ? null : startDate,
                (endDate == null || endDate.isBlank()) ? null : endDate,
                (namePart == null || namePart.isBlank()) ? null : namePart
        );
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param user Objeto usuario a guardar.
     * @return El usuario creado con su ID asignado.
     */
    @Operation(summary = "Crear un nuevo usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@RequestBody User user) {
        return userService.registerNewUserAccount(user);
    }
//    public User create(@RequestBody User user) {
//        return userRepository.save(user);
//    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id ID del usuario a eliminar.
     * @throws UserNotFoundException si el usuario no existe.
     */
    @Operation(summary = "Eliminar un usuario por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario eliminado"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
    @Operation(summary = "Actualizar un usuario existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuario actualizado"),
            @ApiResponse(responseCode = "400", description = "ID no coincide"),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
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
    @Operation(summary = "Asociar un libro a un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro asociado al usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario o libro no encontrado")
    })
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
    @Operation(summary = "Desasociar un libro de un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro desasociado del usuario"),
            @ApiResponse(responseCode = "404", description = "Usuario o libro no encontrado")
    })
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
