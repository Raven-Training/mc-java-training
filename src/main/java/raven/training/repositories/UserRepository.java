package raven.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import raven.training.models.Book;
import raven.training.models.User;

import java.time.LocalDate;
import java.util.List;


/**
 * Repositorio para la entidad {@link User}.
 * Proporciona operaciones CRUD y una consulta personalizada para buscar usuarios por username
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Busca un usuario por el nombre del usuario.
     *
     * @param userName Nombre del autor.
     * @return El usuario asociado al userName, o {@code null} si no se encuentra.
     */
    User findByUserName(String userName);

    List<User> findByBirthDateBetweenAndNameContainingIgnoreCase(LocalDate startDate, LocalDate endDate, String namePart);


}
