package raven.training.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import raven.training.models.Book;
import raven.training.repositories.BookRepository;
import raven.training.exceptions.BookIdMismatchException;
import raven.training.exceptions.BookNotFoundException;

import java.util.List;

/**
 * Controlador REST para gestionar operaciones CRUD sobre libros.
 * Proporciona endpoints para listar, obtener, crear, actualizar y eliminar libros.
 */
@Tag(name = "Books", description = "Operaciones relacionadas con libros")
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    /**
     * Obtiene la lista de todos los libros disponibles.
     *
     * @return una lista de libros.
     */

    @Operation(summary = "Obtener todos los libros")
    @ApiResponse(responseCode = "200", description = "Lista de libros devuelta correctamente")
    @GetMapping
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    /**
     * Busca un libro por su autor.
     *
     * @param author el nombre del autor.
     * @throws BookNotFoundException si no se encuentra el libro con el ID dado.
     * @return el libro correspondiente al autor proporcionado.
     */

    @Operation(summary = "Buscar un libro por autor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Book not found with author")
    })
    @GetMapping("/author/{author}")
    public Book findByAuthor(@PathVariable String author) {
        Book book = bookRepository.findByAuthor(author);
        if (book == null) {
            throw new BookNotFoundException("Book not found with author: " + author);
        }
        return book;
    }

    /**
     * Obtiene un libro por su ID.
     *
     * @param id el ID del libro a buscar.
     * @return el libro correspondiente al ID.
     * @throws BookNotFoundException si no se encuentra el libro con el ID dado.
     */
    @Operation(summary = "Obtener un libro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/{id}")
    public Book findOne(@PathVariable Long id) {
        return bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
    }

    /**
     * Crea un nuevo libro en el sistema.
     *
     * @param book el libro a crear.
     * @return el libro creado.
     */
    @Operation(summary = "Crear un nuevo libro")
    @ApiResponse(responseCode = "201", description = "Libro creado correctamente")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book create(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    /**
     * Elimina un libro por su ID.
     *
     * @param id el ID del libro a eliminar.
     * @throws BookNotFoundException si no se encuentra el libro con el ID dado.
     */
    @Operation(summary = "Eliminar un libro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro eliminado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        bookRepository.deleteById(id);
    }

    /**
     * Actualiza un libro existente.
     *
     * @param book el libro actualizado.
     * @param id el ID del libro a actualizar.
     * @return el libro actualizado.
     * @throws BookIdMismatchException si el ID del libro no coincide con el ID de la ruta.
     * @throws BookNotFoundException si no se encuentra el libro con el ID dado.
     */
    @Operation(summary = "Actualizar un libro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro actualizado"),
            @ApiResponse(responseCode = "400", description = "ID no coincide"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @PutMapping("/{id}")
    public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
        if (book.getId() != id) {
            throw new BookIdMismatchException();
        }
        bookRepository.findById(id)
                .orElseThrow(BookNotFoundException::new);
        return bookRepository.save(book);
    }

}
