package raven.training.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import raven.training.models.User;
import raven.training.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerNewUserAccount(User accountDto) {
        User user = new User();
        user.setName(accountDto.getName());
        user.setBirthDate(accountDto.getBirthDate());
        user.setUserName(accountDto.getUserName());
        user.setBooks(accountDto.getBooks());

        // Cifrar la contraseña
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));

        // Si tienes una lógica para roles, asegúrate que `setRole` esté bien definida
        // user.setRole(new Role(1, user)); ← quítalo si no tienes Role implementado

        return userRepository.save(user);
    }
}
