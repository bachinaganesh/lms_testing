package com.ganesh.lms_testing.integrationtest;

import com.ganesh.lms_testing.TestcontainersConfiguration;
import com.ganesh.lms_testing.dtos.request.RequestAuthorDTO;
import com.ganesh.lms_testing.models.Author;
import com.ganesh.lms_testing.models.enums.Gender;
import com.ganesh.lms_testing.repository.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
public class AuthorControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ModelMapper modelMapper;

    private RequestAuthorDTO requestAuthorDTO;

    @BeforeEach
    void setUp() {
        requestAuthorDTO = RequestAuthorDTO.builder()
                .name("Ganesh")
                .email("ganesh@gmail.com")
                .gender(Gender.MALE)
                .build();
    }

    @AfterEach
    void tearDown() {
        authorRepository.deleteAll();
    }

    @Test
    void testCreateAuthor_WhenValidAuthor_ReturnsCreatedAuthor() {
        webTestClient.post()
                .uri("/authors")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestAuthorDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Ganesh")
                .jsonPath("$.email").isEqualTo("ganesh@gmail.com")
                .jsonPath("$.id").exists();

    }

    @Test
    void testCreateAuthor_WhenInvalidRequestAuthor_ReturnsException() {
        requestAuthorDTO.setEmail("ganesh");
        webTestClient.post()
                .uri("/authors")
                .bodyValue(requestAuthorDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testCreateAuthor_WhenAlreadyExistsAuthor_ReturnsException() {
        authorRepository.save(modelMapper.map(requestAuthorDTO, Author.class));
        webTestClient.post()
                .uri("/authors")
                .bodyValue(requestAuthorDTO)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testGetAuthorById_WhenAuthorExists_ReturnsAuthor() {
        Author author = authorRepository.save(modelMapper.map(requestAuthorDTO, Author.class));
        webTestClient.get()
                .uri("/authors/{id}", author.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Ganesh")
                .jsonPath("$.id").isEqualTo(author.getId())
                .jsonPath("$.email").isEqualTo("ganesh@gmail.com");
    }

    @Test
    void testGetAuthorById_WhenAuthorDoesNotExist_ReturnsException() {
        webTestClient.get()
                .uri("/authors/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteAuthorById_WhenAuthorExists_ReturnsSuccess() {
        Author author = authorRepository.save(modelMapper.map(requestAuthorDTO, Author.class));
        webTestClient.delete()
                .uri("/authors/{id}", author.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("Author with id " + author.getId() + " has been deleted");
    }

    @Test
    void testDeleteAuthorById_WhenAuthorDoesNotExist_ReturnsException() {
        webTestClient.delete()
                .uri("/authors/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testFindAuthorByName_WhenAuthorExists_ReturnsAuthor() {
        Author author = authorRepository.save(modelMapper.map(requestAuthorDTO, Author.class));
        webTestClient.get()
                .uri("/authors?name={name}", "Ganesh")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Ganesh")
                .jsonPath("$.id").isEqualTo(author.getId())
                .jsonPath("$.email").isEqualTo("ganesh@gmail.com");
    }

    @Test
    void testFindAuthorByName_WhenAuthorDoesNotExist_ReturnsException() {
        webTestClient.get()
                .uri("/authors?name={name}", "Ganesh")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateAuthor_WhenAuthorNotExists_ReturnsException() {
        Author author = authorRepository.save(modelMapper.map(requestAuthorDTO, Author.class));
        webTestClient.put()
                .uri("/authors?email={email}", "ganeshb@gmail.com")
                .bodyValue(requestAuthorDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateAuthor_WhenAuthorExistsUpdateEmail_ReturnException() {
        Author author = authorRepository.save(modelMapper.map(requestAuthorDTO, Author.class));
        requestAuthorDTO.setEmail("ganeshb@gmail.com");
        webTestClient.put()
                .uri("/authors?email={email}", "ganesh@gmail.com")
                .bodyValue(requestAuthorDTO)
                .exchange()
                .expectStatus().isBadRequest();

    }

    @Test
    void testUpdateAuthor_WhenAuthorExists_ReturnSuccess() {
        Author author = authorRepository.save(modelMapper.map(requestAuthorDTO, Author.class));
        RequestAuthorDTO updateRequestAuthorDTO = RequestAuthorDTO.builder()
                .name("Suresh")
                .email("ganesh@gmail.com")
                .gender(Gender.MALE)
                .build();

        webTestClient.put()
                .uri("/authors?email={email}", "ganesh@gmail.com")
                .bodyValue(updateRequestAuthorDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.name").isEqualTo("Suresh")
                .jsonPath("$.gender").isEqualTo(Gender.MALE);
    }
}
