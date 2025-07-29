package raven.training.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import raven.training.dtos.OpenLibraryBookDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OpenLibraryService {
    private final RestTemplate restTemplate;

    @Autowired
    public OpenLibraryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<OpenLibraryBookDTO> bookInfo(String isbn) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://openlibrary.org/api/books")
                .queryParam("bibkeys", "ISBN:" + isbn)
                .queryParam("format", "json")
                .queryParam("jscmd", "data")
                .toUriString();

        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        Map<String, Object> bookData = (Map<String, Object>) response.get("ISBN:" + isbn);

        if (bookData == null) return Optional.empty();

        OpenLibraryBookDTO dto = new OpenLibraryBookDTO();
        dto.setIsbn(isbn);
        dto.setTitle((String) bookData.get("title"));
        dto.setSubtitle((String) bookData.getOrDefault("subtitle", ""));

        // Extraer publishers
        List<Map<String, String>> publishers = (List<Map<String, String>>) bookData.get("publishers");
        dto.setPublishers(publishers.stream().map(p -> p.get("name")).collect(Collectors.toList()));

        dto.setPublishDate((String) bookData.get("publish_date"));
        dto.setNumberOfPages((Integer) bookData.get("number_of_pages"));

        // Extraer autores
        List<Map<String, String>> authors = (List<Map<String, String>>) bookData.get("authors");
        dto.setAuthors(authors.stream().map(a -> a.get("name")).collect(Collectors.toList()));

        return Optional.of(dto);
    }
}
