package raven.training.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
    SELECT u FROM User u
    WHERE (:startDate IS NULL OR TO_CHAR(u.birthDate, 'YYYY-MM-DD') >= :startDate)
      AND (:endDate IS NULL OR TO_CHAR(u.birthDate, 'YYYY-MM-DD') <= :endDate)
      AND (:namePart IS NULL OR LOWER(u.name) LIKE LOWER(CONCAT('%', :namePart, '%')))
""")
    List<User> searchUsers(
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("namePart") String namePart
    );





}
