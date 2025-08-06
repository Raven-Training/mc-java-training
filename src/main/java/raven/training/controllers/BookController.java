package raven.training.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import raven.training.dtos.OpenLibraryBookDTO;
import raven.training.models.Book;
import raven.training.repositories.BookRepository;
import raven.training.exceptions.BookIdMismatchException;
import raven.training.exceptions.BookNotFoundException;
import raven.training.services.OpenLibraryService;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para gestionar operaciones CRUD sobre libros.
 * Proporciona endpoints para listar, obtener, crear, actualizar y eliminar libros.
 */
@Tag(name = "Books", description = "Operaciones relacionadas con libros")
@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private OpenLibraryService openLibraryService;

    /**
     * Obtiene la lista de todos los libros disponibles.
     *
     * @return una lista de libros.
     */

//    @Operation(summary = "Obtener todos los libros")
//    @ApiResponse(responseCode = "200", description = "Lista de libros devuelta correctamente")
//    @GetMapping
//    public List<Book> findAll() {
//        return bookRepository.findAll();
//    }

    @Operation(summary = "Obtener todos los libros")
    @ApiResponse(responseCode = "200", description = "Lista de libros devuelta correctamente")
    @GetMapping
    public Page<Book> getAllBooks(
            @RequestParam Optional<String> genre,
            @RequestParam Optional<String> author,
            @RequestParam Optional<String> title,
            @RequestParam Optional<String> subtitle,
            @RequestParam Optional<String> publisher,
            @RequestParam Optional<String> year,
            @RequestParam Optional<Integer> pages,
            @RequestParam Optional<String> isbn,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        String cleanedGenre = genre.filter(s -> !s.isBlank()).orElse(null);
        String cleanedAuthor = author.filter(s -> !s.isBlank()).orElse(null);
        String cleanedTitle = title.filter(s -> !s.isBlank()).orElse(null);
        String cleanedSubtitle = subtitle.filter(s -> !s.isBlank()).orElse(null);
        String cleanedPublisher = publisher.filter(s -> !s.isBlank()).orElse(null);
        String cleanedYear = year.filter(s -> !s.isBlank()).orElse(null);
        String cleanedIsbn = isbn.filter(s -> !s.isBlank()).orElse(null);
        Integer cleanedPages = pages.orElse(null);
        System.out.printf("genre: %s, author: %s, title: %s, subtitle: %s, publisher: %s, year: %s, isbn: %s, pages: %s%n",
                cleanedGenre, cleanedAuthor, cleanedTitle, cleanedSubtitle, cleanedPublisher, cleanedYear, cleanedIsbn, cleanedPages);




        return bookRepository.searchBooksByFilters(
                cleanedGenre,
                cleanedAuthor,
                cleanedTitle,
                cleanedSubtitle,
                cleanedPublisher,
                cleanedYear,
                cleanedIsbn,
                cleanedPages,
                pageable
        );
    }


//    public List<Book> getAllBooks(
//            @RequestParam(required = false) String genre,
//            @RequestParam(required = false) String author,
//            @RequestParam(required = false) String title,
//            @RequestParam(required = false) String subtitle,
//            @RequestParam(required = false) String publisher,
//            @RequestParam(required = false) String year,
//            @RequestParam(required = false) Integer pages,
//            @RequestParam(required = false) String isbn
//    ) {
//        return bookRepository.searchBooksByFilters(
//                blankToNull(genre),
//                blankToNull(author),
//                blankToNull(title),
//                blankToNull(subtitle),
//                blankToNull(publisher),
//                blankToNull(year),
//                pages,
//                blankToNull(isbn)
//        );
//    }

    // Método utilitario
    private String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value;

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

    @Operation(summary = "Obtener un libro por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado")
    })
    @GetMapping("/match/{publisher}/{genre}/{year}")
    public List<Book> findByPublisherAndGenreAndYear(@PathVariable String publisher,@PathVariable  String genre, @PathVariable String year) {
        return bookRepository.findByPublisherAndGenreAndYear(publisher,genre,year);
    }

    @GetMapping("/search")
    public Page<Book> searchBooks(
            @RequestParam(required = false) String publisher,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String year,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "true") boolean ascending
    ) {

        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.searchBooks(
                publisher == null || publisher.isBlank() ? null : publisher,
                genre == null || genre.isBlank() ? null : genre,
                year == null || year.isBlank() ? null : year,
                pageable
        );
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

    @PostMapping("/isbn/{isbn}")
    public ResponseEntity<?> findByIsbn(@PathVariable String isbn) {
        Optional<Book> localBook = bookRepository.findByIsbn(isbn);

        if (localBook.isPresent()) {
            return ResponseEntity.ok(localBook.get());
        }

        Optional<OpenLibraryBookDTO> externalBookOpt = openLibraryService.bookInfo(isbn);
        if (externalBookOpt.isPresent()) {
            OpenLibraryBookDTO dto = externalBookOpt.get();

            Book book = new Book();
            book.setIsbn(dto.getIsbn());
            book.setTitle(dto.getTitle());
            book.setSubtitle(dto.getSubtitle());
            book.setPublisher(String.join(", ", dto.getPublishers()));
            book.setAuthor(String.join(", ", dto.getAuthors()));
            book.setYear(dto.getPublishDate());
            book.setPages(dto.getNumberOfPages());
            book.setImage(""); // La API no da imagen directa
            book.setGenre("Default");

            bookRepository.save(book);
            return ResponseEntity.status(HttpStatus.CREATED).body(book);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found in OpenLibrary");
    }

}
