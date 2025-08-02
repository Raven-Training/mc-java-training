package raven.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import raven.training.models.Book;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad {@link Book}.
 * Proporciona operaciones CRUD y una consulta personalizada para buscar libros por autor.
 */
@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    /**
     * Busca un libro por el nombre del autor.
     *
     * @param author Nombre del autor.
     * @return El libro asociado al autor, o {@code null} si no se encuentra.
     */
    Book findByAuthor(String author);

    Optional<Book> findByIsbn(String isbn);

    List<Book> findByPublisherAndGenreAndYear(String publisher, String genre, String year);

    @Query("""
    SELECT b FROM Book b
    WHERE (:publisher IS NULL OR b.publisher = :publisher)
      AND (:genre IS NULL OR b.genre = :genre)
      AND (:year IS NULL OR b.year = :year)
""")
    List<Book> searchBooks(
            @Param("publisher") String publisher,
            @Param("genre") String genre,
            @Param("year") String year
    );


}
