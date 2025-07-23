package raven.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raven.training.models.Book;
import raven.training.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
}
