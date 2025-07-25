package raven.training.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import raven.training.models.User;
import raven.training.models.Book;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenCreatingUser_thenReturn201() throws Exception {
        User newUser = new User("testuser", "password", "Test Name", LocalDate.of(2000, 1, 1), Collections.emptyList());

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenCreatingBook_thenReturn201() throws Exception {
        Book newBook = new Book("Terror", "Autor", "Author", "Nuevo libro", "subtitle"
                ,"publiser", "2024", 100, "qwerty",  new ArrayList<>());

        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newBook)))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenAccessingProtectedEndpointWithoutAuth_thenUnauthorized() throws Exception {
        // Suponiendo que esta ruta sí requiere autenticación
        mockMvc.perform(get("/protected-endpoint"))
                .andExpect(status().isUnauthorized());
    }
}

