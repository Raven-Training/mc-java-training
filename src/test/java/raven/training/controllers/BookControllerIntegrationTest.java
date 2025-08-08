package raven.training.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
import raven.training.services.OpenLibraryService;
import raven.training.services.UserService;

import java.util.*;

@WebMvcTest(BookController.class)
@Import(SecurityConfig.class)
@ActiveProfiles("test")
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private OpenLibraryService openLibraryService;

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

    @Test
    void whenGettingAllBooks_thenStatus200() throws Exception {
//        Book book = new Book("Genero","author", "imagen","titulo","subtitulo","editorial"
//                ,"año",90,"isbn", Collections.emptyList());


        mockMvc.perform(get("/api/books")
                        .with(httpBasic("mery333", "1234"))
                )
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreateBook_thenReturn201() throws Exception {


        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setTitle("Spring Boot in Action");
        savedBook.setAuthor("Craig Walls");

//        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        mockMvc.perform(post("/api/books")
//                        .with(httpBasic("mery333", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedBook)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenGetBookById_thenReturn200() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Spring Boot in Action");
        book.setAuthor("Craig Walls");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(get("/api/books/1")
                        .with(httpBasic("mery333", "1234")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Spring Boot in Action"));
    }

    @Test
    public void whenGetNonExistentBook_thenReturn404() throws Exception {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/books/99")
                        .with(httpBasic("mery333", "1234")))
                .andExpect(status().isNotFound());
    }


    @Test
    public void whenUpdateBookWithMatchingId_thenReturn200() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Updated Title");
        book.setAuthor("Updated Author");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        mockMvc.perform(put("/api/books/1")
                        .with(httpBasic("mery333", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Title"));
    }

    @Test
    public void whenUpdateBookWithMismatchedId_thenReturn400() throws Exception {
        Book book = new Book();
        book.setId(2L);
        book.setTitle("Mismatch");
        book.setAuthor("Author");

        mockMvc.perform(put("/api/books/1")
                        .with(httpBasic("mery333", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateNonExistentBook_thenReturn404() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Not Found");
        book.setAuthor("Author");

        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/books/1")
                        .with(httpBasic("mery333", "1234"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenDeleteExistingBook_thenReturn200() throws Exception {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("To Delete");

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        mockMvc.perform(delete("/api/books/1")
                        .with(httpBasic("mery333", "1234")))
                .andExpect(status().isOk());

        verify(bookRepository).deleteById(1L);
    }

    @Test
    public void whenDeleteNonExistentBook_thenReturn404() throws Exception {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/books/99")
                        .with(httpBasic("mery333", "1234")))
                .andExpect(status().isNotFound());
    }

}
