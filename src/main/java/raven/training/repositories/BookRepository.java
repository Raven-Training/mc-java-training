package raven.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raven.training.models.Book;


public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByAuthor(String author);
}
