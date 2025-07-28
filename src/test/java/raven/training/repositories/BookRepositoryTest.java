package raven.training.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import raven.training.models.Book;

import java.util.Collections;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
@EntityScan(basePackageClasses = Book.class)
public class BookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookRepository bookRepository;

    private Book buildValidBook() {
        return new Book(
                "Ficción",
                "Gabriel García Márquez",
                "imagen.jpg",
                "Cien años de soledad",
                "Una historia mágica",
                "Editorial Sudamericana",
                "1967",
                432,
                "978-3-16-148410-0",
                Collections.emptyList()
        );
    }

    @Test
    @DisplayName("Debe guardar un libro válido correctamente")
    void saveValidBook_shouldSucceed() {
        Book book = buildValidBook();
        Book saved = bookRepository.save(book);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getTitle()).isEqualTo("Cien años de soledad");
    }

    @Test
    @DisplayName("Debe lanzar excepción si falta el título")
    void saveBookWithoutTitle_shouldFail() {
        Book book = buildValidBook();
        book.setTitle(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            bookRepository.saveAndFlush(book);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción si falta el autor")
    void saveBookWithoutAuthor_shouldFail() {

        Book book = buildValidBook();
        book.setAuthor(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            bookRepository.saveAndFlush(book);
        });
    }

    @Test
    @DisplayName("Debe encontrar un libro por autor")
    void findByAuthor_shouldReturnBook() {
        Book book = buildValidBook();
        bookRepository.saveAndFlush(book);

        Book found = bookRepository.findByAuthor("Gabriel García Márquez");

        assertThat(found).isNotNull();
        assertThat(found.getTitle()).isEqualTo("Cien años de soledad");
    }

    @Test
    @DisplayName("No debe encontrar libro si autor no existe")
    void findByAuthor_whenNotExists_shouldReturnNull() {
        Book found = bookRepository.findByAuthor("Autor Inexistente");
        assertThat(found).isNull();
    }
}
