package raven.training.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;


import raven.training.models.User;
import raven.training.models.Book;
import raven.training.repositories.BookRepository;
import raven.training.repositories.UserRepository;
import raven.training.security.SecurityConfig;
import raven.training.services.UserService;

import java.time.LocalDate;
import java.util.*;

//@SpringBootTest
//@AutoConfigureMockMvc(addFilters = false)
//@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUserName("mery333");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findByUserName("mery333")).thenReturn(user);
    }

//    void setUp() {
//        raven.training.models.User user = new raven.training.models.User();
//        user.setUserName("mery333");
//        user.setPassword(passwordEncoder.encode("1234")); // importante codificar
//        userRepository.save(user);
//    }

    @Test
    public void whenCreatingUser_thenReturn201() throws Exception {
        User newUser = new User("testuser", "password", "Test Name", LocalDate.of(2000, 1, 1), Collections.emptyList());
//        User user = new User();
//        user.setUserName("mery333");
//        user.setPassword(passwordEncoder.encode("1234")); // o el encoder que uses
//        userRepository.save(user);
        mockMvc.perform(post("/api/users")
                        .with(httpBasic("mery333", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenGettingAllUsers_thenStatus200() throws Exception {
        mockMvc.perform(get("/api/users")
                        .with(httpBasic("mery333", "1234")))
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetUserById_thenReturnUserAndStatus200() throws Exception {
        // Asegúrate que un usuario con ID 1 exista en la DB antes del test
        User user = new User();
        user.setId(54L);
        user.setUserName("mery333");
        user.setPassword(passwordEncoder.encode("1234"));

        when(userRepository.findById(54L)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/api/users/54").with(httpBasic("mery333", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(54));
    }

    @Test
    public void whenGetUserByInvalidId_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/users/9999").with(httpBasic("mery333", "1234")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetUserByUsername_thenReturnUser() throws Exception {
        User user = new User();
        user.setId(54L);
        user.setUserName("fruiz55Edit");
        // completa los campos necesarios

        when(userRepository.findByUserName("fruiz55Edit")).thenReturn(user);
        mockMvc.perform(get("/api/users/username/fruiz55Edit").with(httpBasic("mery333", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("fruiz55Edit"));
    }

    @Test
    public void whenGetUserByNonExistentUsername_thenReturn404() throws Exception {
        mockMvc.perform(get("/api/users/username/notfound").with(httpBasic("mery333", "1234")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateUserWithMatchingId_thenReturn200() throws Exception {
//        User updatedUser = new User(1L,"updatedUser", "password", "Updated Name", LocalDate.of(1990, 1, 1), Collections.emptyList());
        User updatedUser = new User();
        updatedUser.setId(52L);
        updatedUser.setUserName("updatedUser");
        updatedUser.setPassword("password");
        updatedUser.setName("Updated Name");
        updatedUser.setBirthDate(LocalDate.of(1990, 1, 1));
        updatedUser.setBooks(Collections.emptyList());
        when(userRepository.findById(52L)).thenReturn(Optional.of(updatedUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        mockMvc.perform(put("/api/users/52")
                        .with(httpBasic("mery333", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("updatedUser"));
    }

    @Test
    public void whenUpdateUserWithMismatchingId_thenReturn400() throws Exception {
//        User updatedUser = new User(2L, "updatedUser", "password", "Updated Name", LocalDate.of(1990, 1, 1), List.of());
        User updatedUser = new User();
        updatedUser.setId(2L);
        updatedUser.setUserName("updatedUser");
        updatedUser.setPassword("password");
        updatedUser.setName("Updated Name");
        updatedUser.setBirthDate(LocalDate.of(1990, 1, 1));
        updatedUser.setBooks(Collections.emptyList());

        mockMvc.perform(put("/api/users/1")
                        .with(httpBasic("mery333", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedUser)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenDeleteExistingUser_thenStatus200() throws Exception {
        User user = new User();
        user.setId(54L);
        user.setUserName("mery333");
        // completa los campos necesarios

        when(userRepository.findById(54L)).thenReturn(Optional.of(user));
        mockMvc.perform(delete("/api/users/54").with(httpBasic("mery333", "1234")))
                .andExpect(status().isOk());
    }

    @Test
    public void whenDeleteNonExistentUser_thenReturn404() throws Exception {
        mockMvc.perform(delete("/api/users/9999").with(httpBasic("mery333", "1234")))
                .andExpect(status().isNotFound());
    }


    @Test
    public void whenAddBookToUser_thenReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId(53L);
        user.setUserName("mery333");
        user.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(88L);
        book.setTitle("Spring Boot in Action");

//        user.addBook(book); // opcional: si el método hace esto


        when(userRepository.findById(53L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(3L)).thenReturn(Optional.of(book));
        when(userRepository.save(any(User.class))).thenReturn(user);
        mockMvc.perform(put("/api/users/53/books/3").with(httpBasic("mery333", "1234")))
                .andExpect(status().isOk());
    }

    @Test
    public void whenRemoveBookFromUser_thenReturnUpdatedUser() throws Exception {
        User user = new User();
        user.setId(53L);
        user.setUserName("mery333");
        user.setBooks(new ArrayList<>());

        Book book = new Book();
        book.setId(88L);
        book.setTitle("Spring Boot in Action");

        when(userRepository.findById(53L)).thenReturn(Optional.of(user));
        when(bookRepository.findById(3L)).thenReturn(Optional.of(book));
        when(userRepository.save(any(User.class))).thenReturn(user);
        mockMvc.perform(delete("/api/users/53/books/3").with(httpBasic("mery333", "1234")))
                .andExpect(status().isOk());
    }











//    @Test
//    public void whenCreatingBook_thenReturn201() throws Exception {
//        Book newBook = new Book("Terror", "Autor", "Author", "Nuevo libro", "subtitle"
//                ,"publiser", "2024", 100, "qwerty",  new ArrayList<>());
//
//        mockMvc.perform(post("/api/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(newBook)))
//                .andExpect(status().isCreated());
//    }

    @Test
    public void whenAccessingProtectedEndpointWithoutAuth_thenUnauthorized() throws Exception {
        // Suponiendo que esta ruta sí requiere autenticación
        mockMvc.perform(get("/protected-endpoint"))
                .andExpect(status().isUnauthorized());
    }
}

