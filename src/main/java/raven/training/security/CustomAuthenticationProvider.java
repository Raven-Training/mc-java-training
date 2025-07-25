package raven.training.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.security.core.userdetails.User;
import raven.training.exceptions.UserNotFoundException;
import raven.training.repositories.UserRepository;


import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

//    @Override
//    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
//        final String name = authentication.getName();
//        final String password = authentication.getCredentials().toString();
//        if (!"admin".equals(name) || !"system".equals(password)) {
//            return null;
//        }
//        return authenticateAgainstThirdPartyAndGetAuthentication(name, password);
//    }
//
//    @Override
//    public boolean supports(Class<?> authentication) {
//        return authentication.equals(UsernamePasswordAuthenticationToken.class);
//    }
//
//    private static UsernamePasswordAuthenticationToken authenticateAgainstThirdPartyAndGetAuthentication(String name, String password) {
//        final List<GrantedAuthority> grantedAuths = new ArrayList<>();
//        grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
//        final UserDetails principal = new User(name, password, grantedAuths);
//        return new UsernamePasswordAuthenticationToken(principal, password, grantedAuths);
//    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String username = authentication.getName();
        final String rawPassword = authentication.getCredentials().toString();

        // Buscar el usuario por nombre
        raven.training.models.User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UserNotFoundException("User not found with username: " + username);
        }

        // Verificar la contraseña usando el encoder (por ejemplo BCrypt)
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Asignar roles (puedes extraerlos de tu entidad si los tienes)
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));

        UserDetails principal = new User(user.getUserName(), user.getPassword(), authorities);
        return new UsernamePasswordAuthenticationToken(principal, rawPassword, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
