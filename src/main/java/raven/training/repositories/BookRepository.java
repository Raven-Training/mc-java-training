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
    @Query("""
    SELECT b FROM Book b
    WHERE (:genre IS NULL OR LOWER(b.genre) = LOWER(:genre))
      AND (:author IS NULL OR LOWER(b.author) = LOWER(:author))
      AND (:title IS NULL OR LOWER(b.title) = LOWER(:title))
      AND (:subtitle IS NULL OR LOWER(b.subtitle) = LOWER(:subtitle))
      AND (:publisher IS NULL OR LOWER(b.publisher) = LOWER(:publisher))
      AND (:year IS NULL OR b.year = :year)
      AND (:isbn IS NULL OR b.isbn = :isbn)
      AND (:pages IS NULL OR b.pages = :pages)
""")
    List<Book> searchBooksByFilters(
            @Param("genre") String genre,
            @Param("author") String author,
            @Param("title") String title,
            @Param("subtitle") String subtitle,
            @Param("publisher") String publisher,
            @Param("year") String year,
            @Param("pages") Integer pages,
            @Param("isbn") String isbn
    );

//    @Query("""
//    SELECT b FROM Book b
//    WHERE (:genre IS NULL OR LOWER(b.genre) = LOWER(:genre))
//      AND (:author IS NULL OR LOWER(b.author) = LOWER(:author))
//      AND (:title IS NULL OR LOWER(b.title) = LOWER(:title))
//      AND (:subtitle IS NULL OR LOWER(b.subtitle) = LOWER(:subtitle))
//      AND (:publisher IS NULL OR LOWER(b.publisher) = LOWER(:publisher))
//      AND (:year IS NULL OR b.year = :year)
//      AND (:isbn IS NULL OR b.isbn = :isbn)
//      AND (:pages IS NULL OR b.pages = :pages)
//""")
//    List<Book> searchBooksByFilters(
//            @Param("genre") String genre,
//            @Param("author") String author,
//            @Param("title") String title,
//            @Param("subtitle") String subtitle,
//            @Param("publisher") String publisher,
//            @Param("year") String year,
//            @Param("pages") Integer pages,
//            @Param("isbn") String isbn
//    );




}
