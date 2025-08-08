package raven.training.controllers;


import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@WireMockTest(httpPort = 8181)
public class OpenLibraryIntTest {


    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.options().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @Test
    public void getFirstReturn(WireMockRuntimeInfo wireMockRuntimeInfo) {
        System.out.println(wireMockRuntimeInfo.getHttpBaseUrl() +" " + wireMockRuntimeInfo.getHttpPort());
    }

    @Test
    public void returnBookFound() throws IOException, InterruptedException {
        // Setup the WireMock mapping stub for the test
        stubFor(post("/api/books/isbn/0378945615")
                .withHeader("Content-Type", containing("xml"))
                .willReturn(ok()
                        .withHeader("Content-Type", "text/xml")
                        .withBody("""
                        {
                            "id": 252,
                            "genre": "Default",
                            "author": "Princeton",
                            "image": "",
                            "title": "Swindled!",
                            "subtitle": "classic business frauds of the seventies",
                            "publisher": "Dow Jones Books",
                            "year": "1976",
                            "pages": 176,
                            "isbn": "0871285177",
                            "users": null
                        }
                    """)));

        // Setup HTTP POST request (with HTTP Client embedded in Java 11+)
        final HttpClient client = HttpClient.newBuilder().build();
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(wireMockServer.url("/api/books/isbn/0378945615")))
                .header("Content-Type", "text/xml")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();


        // Send the request and receive the response
        final HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verify the response (with AssertJ)
        assertThat(response.statusCode()).as("Wrong response status code").isEqualTo(200);
        assertThat(response.body()).as("Wrong response body").contains("\"title\": \"Swindled!\"");
    }

    @Test
    public void shouldReturnNotFoundWhenIsbnIsMissing() throws IOException, InterruptedException {
        // Configura el stub para simular la respuesta de ISBN no encontrado
        stubFor(post("/api/books/isbn/9999999999")
                .withHeader("Content-Type", containing("xml"))
                .willReturn(aResponse()
                        .withStatus(404)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Book not found in openLibrary\"}")));

        // Configura el cliente HTTP
        final HttpClient client = HttpClient.newBuilder().build();
        final HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(wireMockServer.url("/api/books/isbn/9999999999")))
                .header("Content-Type", "text/xml")
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        // Ejecuta la petición
        final HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

        // Verifica que la respuesta sea 404 con el mensaje esperado
        assertThat(response.statusCode()).as("Expected 404 for missing ISBN").isEqualTo(404);
        assertThat(response.body()).as("Expected book not found message").contains("Book not found in openLibrary");
    }

}