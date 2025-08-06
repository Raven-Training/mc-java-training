package raven.training.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Representa un libro dentro del sistema.
 * Cada libro contiene información bibliográfica y puede estar asociado a múltiples usuarios.
 */
@Entity
@Table(name = "book")
@Schema(description = "Entidad que representa un libro con información bibliográfica.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Identificador único del libro", example = "1")
    private Long id;

    @Column(nullable = true)
    @Schema(description = "Género del libro", example = "Ficción")
    private String genre;

    @Column(nullable = false)
    @Schema(description = "Nombre del autor del libro", example = "Gabriel García Márquez")
    private String author;

    @Column(nullable = false)
    @Schema(description = "URL o nombre de archivo de la imagen de portada", example = "imagen.jpg")
    private String image;

    @Column(nullable = false)
    @Schema(description = "Título principal del libro", example = "Cien años de soledad")
    private String title;

    @Column(nullable = false)
    @Schema(description = "Subtítulo del libro", example = "Una historia mágica")
    private String subtitle;

    @Column(nullable = false)
    @Schema(description = "Editorial del libro", example = "Editorial Sudamericana")
    private String publisher;

    @Column(name = "book_year", nullable = false)
    @Schema(description = "Año de publicación", example = "1967")
    private String year;

    @Column(nullable = false)
    @Schema(description = "Número de páginas del libro", example = "432")
    private Integer pages;

    @Column(nullable = false)
    @Schema(description = "Código ISBN del libro", example = "978-3-16-148410-0")
    private String isbn;

    @ManyToMany(mappedBy = "books")
    @Schema(description = "Lista de usuarios asociados al libro (relación ManyToMany)")
    private List<User> users;

    /**
     * Constructor protegido requerido por JPA.
     */
//    public Book() {}

    /**
     * Constructor para crear una instancia manual de Book.
     *
     * @param genre     Género del libro.
     * @param author    Autor del libro.
     * @param image     URL o nombre de archivo de la imagen de portada.
     * @param title     Título principal del libro.
     * @param subtitle  Subtítulo del libro.
     * @param publisher Editorial del libro.
     * @param year      Año de publicación.
     * @param pages     Número de páginas.
     * @param isbn      Código ISBN del libro.
     * @param users     Lista de usuarios que tienen este libro
     */
    public Book(String genre, String author, String image, String title, String subtitle,
                String publisher, String year, Integer pages, String isbn, List<User> users) {
        this.genre = genre;
        this.author = author;
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
        this.publisher = publisher;
        this.year = year;
        this.pages = pages;
        this.isbn = isbn;
        this.users = users;
    }


}