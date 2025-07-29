package raven.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raven.training.models.Book;

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
}
