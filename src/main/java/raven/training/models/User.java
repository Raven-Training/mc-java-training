package raven.training.models;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import raven.training.exceptions.BookAlreadyOwnedException;

/**
 * Representa un usuario dentro del sistema.
 * Un usuario tiene un nombre de usuario, un nombre completo,
 * una fecha de nacimiento y una lista de libros asociados.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDate birthDate;

    @ManyToMany
    @JoinTable(
            name = "user_books",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private List<Book> books = new ArrayList<>();


    /**
     * Constructor protegido requerido por JPA.
     */
    protected User() {}

    /**
     * Constructor para crear manualmente una instancia de User.
     *
     * @param userName  Nombre de usuario único.
     * @param name      Nombre completo del usuario.
     * @param birthDate Fecha de nacimiento del usuario.
     * @param books     Lista de libros asociados al usuario.
     */
    public User(String userName, String name, LocalDate birthDate, List<Book> books) {
        this.userName = userName;
        this.name = name;
        this.birthDate = birthDate;
        this.books = books;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }


    /**
     * Devuelve una lista inmodificable de los libros asociados al usuario.
     *
     * @return Lista de libros del usuario.
     */
    public List<Book> getBooks() {
        return Collections.unmodifiableList(books);
    }

    /**
     * Establece la lista de libros asociados al usuario.
     *
     * @param books Nueva lista de libros.
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Asocia un nuevo libro al usuario si aún no lo tiene.
     *
     * @param book Libro a asociar.
     * @throws BookAlreadyOwnedException Si el libro ya está asociado al usuario.
     */
    public void addBook(Book book) {
        if (books.contains(book)) {
            throw new BookAlreadyOwnedException("El libro ya está asociado a este usuario");
        }
        books.add(book);
    }

    /**
     * Elimina un libro de la lista de libros del usuario.
     *
     * @param book Libro a eliminar.
     */
    public void removeBook(Book book) {
        books.remove(book);
    }

}
