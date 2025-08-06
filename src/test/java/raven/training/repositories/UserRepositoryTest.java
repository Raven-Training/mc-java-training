package raven.training.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import raven.training.models.User;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Debería encontrar un usuario por su userName")
    public void testFindByUserName() {
        // Arrange
        User user = new User();
        user.setUserName("miguel123");
        user.setPassword("1234");
        user.setName("Miguel Castaño");
        user.setBirthDate(LocalDate.of(2000, 1, 1));
        entityManager.persistAndFlush(user);

        // Act
        User found = userRepository.findByUserName("miguel123");

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getUserName()).isEqualTo("miguel123");
        assertThat(found.getName()).isEqualTo("Miguel Castaño");
    }

    @Test
    @DisplayName("Debería devolver null si el usuario no existe")
    public void testFindByUserName_notFound() {
        // Act
        User found = userRepository.findByUserName("noExiste");

        // Assert
        assertThat(found).isNull();
    }

    @Test
    @DisplayName("Debería guardar un usuario correctamente")
    public void testSaveUser() {
        // Arrange
        User user = new User();
        user.setUserName("lucia456");
        user.setPassword("abcd");
        user.setName("Lucía Pérez");
        user.setBirthDate(LocalDate.of(1995, 5, 15));

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUserName()).isEqualTo("lucia456");
    }



    @Test
    @DisplayName("Debe fallar al persistir sin userName")
    void persistWithoutUserName_shouldFail() {
        User user = new User();
        user.setPassword("1234");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }

    @Test
    @DisplayName("Debe fallar al persistir sin password")
    void persistWithoutPassword_shouldFail() {
        User user = new User();
        user.setUserName("noPassUser");

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);
        });
    }


}
