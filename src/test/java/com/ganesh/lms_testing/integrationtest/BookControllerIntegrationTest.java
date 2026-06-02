package com.ganesh.lms_testing.integrationtest;

import com.ganesh.lms_testing.TestcontainersConfiguration;
import com.ganesh.lms_testing.dtos.request.RequestBookDTO;
import com.ganesh.lms_testing.models.Author;
import com.ganesh.lms_testing.models.Book;
import com.ganesh.lms_testing.models.enums.Gender;
import com.ganesh.lms_testing.repository.AuthorRepository;
import com.ganesh.lms_testing.repository.BookRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
public class BookControllerIntegrationTest {

    @Autowired
    private BookRepository  bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ModelMapper modelMapper;

    private RequestBookDTO requestBookDTO;
    private Author author;

    @BeforeEach
    public void setup() {
        requestBookDTO = RequestBookDTO.builder()
                .title("Java")
                .price(500.0)
                .build();

        author = Author.builder()
                .name("Ganesh")
                .email("ganesh@gmail.com")
                .gender(Gender.MALE)
                .build();
    }

    @AfterEach
    public void tearDown() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
    }

    @Test
    void testCreateBook_WhenValidBookDTO_ShouldCreateBook() {
        Author savedAuthor = authorRepository.save(author);
        webTestClient.post()
                .uri("/books?authorId={authorId}", savedAuthor.getId())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBookDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Java")
                .jsonPath("$.price").isEqualTo(500.0)
                .jsonPath("$.author.name").isEqualTo("Ganesh")
                .jsonPath("$.author.email").isEqualTo("ganesh@gmail.com");
    }

    @Test
    void testCreateBook_WhenAuthorNotExists_ShouldReturnException() {
        webTestClient.post()
                .uri("/books?authorId={authorId}", 1L)
                .bodyValue(requestBookDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testCreateBook_WhenInvalidRequest_ShouldReturnException() {
        requestBookDTO.setPrice(1200.0);

        webTestClient.post()
                .uri("/books?authorId={authorId}", 1L)
                .bodyValue(requestBookDTO)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void testGetBookById_WhenBookNotExists_ShouldReturnException() {
        webTestClient.get()
                .uri("/books/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetBookById_WhenBookExists_ShouldReturnBook() {
        Author savedAuthor = authorRepository.save(author);

        Book book = modelMapper.map(requestBookDTO, Book.class);
        book.setAuthor(savedAuthor);
        Book savedBook = bookRepository.save(book);

        webTestClient.get()
                .uri("/books/{id}", savedBook.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Java")
                .jsonPath("$.price").isEqualTo(500.0)
                .jsonPath("$.author.name").isEqualTo("Ganesh")
                .jsonPath("$.author.email").isEqualTo("ganesh@gmail.com");
    }

    @Test
    void testDeleteBookById_WhenBookNotExists_ShouldReturnException() {
        webTestClient.delete()
                .uri("/books/{id}", 1L)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testDeleteBookById_WhenBookExists_ShouldReturnSuccess() {
        Author savedAuthor = authorRepository.save(author);
        Book book = modelMapper.map(requestBookDTO, Book.class);
        book.setAuthor(savedAuthor);
        Book savedBook = bookRepository.save(book);

        webTestClient.delete()
                .uri("/books/{id}", savedBook.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class);
    }

    @Test
    void testGetBookByTitle_WhenBookNotExists_ShouldReturnException() {
        webTestClient.get()
                .uri("/books?title={title}", "Java")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testGetBookByTitle_WhenBookExists_ShouldReturnBook() {
        Author savedAuthor = authorRepository.save(author);
        Book book = modelMapper.map(requestBookDTO, Book.class);
        book.setAuthor(savedAuthor);
        Book savedBook = bookRepository.save(book);
        webTestClient.get()
                .uri("/books?title={title}", book.getTitle())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Java")
                .jsonPath("$.price").isEqualTo(500.0)
                .jsonPath("$.author.name").isEqualTo("Ganesh")
                .jsonPath("$.author.email").isEqualTo("ganesh@gmail.com");
    }

    @Test
    void testUpdateBookById_WhenBookNotExists_ShouldReturnException() {
        webTestClient.put()
                .uri("/books/{bookId}", 1L)
                .bodyValue(requestBookDTO)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void testUpdateBookById_WhenBookExists_ShouldReturnSuccess() {
        Author savedAuthor = authorRepository.save(author);
        Book book = modelMapper.map(requestBookDTO, Book.class);
        book.setAuthor(savedAuthor);
        Book savedBook = bookRepository.save(book);

        RequestBookDTO updateRequestBookDTO = RequestBookDTO.builder()
                .title("Python")
                .price(200.0)
                .build();

        webTestClient.put()
                .uri("/books/{bookId}", savedBook.getId())
                .bodyValue(updateRequestBookDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.title").isEqualTo("Python")
                .jsonPath("$.price").isEqualTo(200.0)
                .jsonPath("$.author.name").isEqualTo("Ganesh")
                .jsonPath("$.author.email").isEqualTo("ganesh@gmail.com");

    }
}
