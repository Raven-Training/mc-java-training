package raven.training.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Raven Training API",
                version = "1.0.0",
                description = "API para crear libros y usuarios",
                contact = @Contact(name = "Miguel Angel Castaño", email = "miguel.castano@raven.inc"),
                license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT ")
        ),
        servers = {
                @Server(url = "http://localhost:8081", description = "Servidor local")
        }
)
public class SwaggerConfig {
    // Puedes agregar configuraciones adicionales aquí si usas seguridad
}
