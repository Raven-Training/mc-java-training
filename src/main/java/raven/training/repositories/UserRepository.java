package raven.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import raven.training.models.Book;
import raven.training.models.User;


/**
 * Repositorio para la entidad {@link User}.
 * Proporciona operaciones CRUD y una consulta personalizada para buscar usuarios por username
 */
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Busca un usuario por el nombre del usuario.
     *
     * @param userName Nombre del autor.
     * @return El usuario asociado al userName, o {@code null} si no se encuentra.
     */
    User findByUserName(String userName);

}
