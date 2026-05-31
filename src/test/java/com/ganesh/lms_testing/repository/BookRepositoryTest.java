package com.ganesh.lms_testing.repository;

import com.ganesh.lms_testing.TestcontainersConfiguration;
import com.ganesh.lms_testing.models.Author;
import com.ganesh.lms_testing.models.Book;
import com.ganesh.lms_testing.models.enums.Gender;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Book book;
    private Author author;

    @BeforeEach
    void setup() {
        author = Author.builder()
                .name("Ganesh")
                .gender(Gender.MALE)
                .email("ganesh@gmail.com")
                .build();

        book = Book.builder()
                .title("Java")
                .price(500.0)
                .build();
    }

    @Test
    public void testFindByTitleIgnoreCase_ShouldReturnBook() {
        Author savedAuthor = authorRepository.save(author);
        book.setAuthor(savedAuthor);
        bookRepository.save(book);
        Optional<Book> optionalBook = bookRepository.findByTitleIgnoreCase("java");
        Assertions.assertThat(optionalBook.isPresent()).isTrue();
        Assertions.assertThat(optionalBook.get()).isEqualTo(book);
        Assertions.assertThat(optionalBook.get().getAuthor()).isEqualTo(savedAuthor);
    }

    @Test
    public void testFindByTitleIgnoreCase_ShouldReturnEmptyOptional() {
        Optional<Book> optionalBook = bookRepository.findByTitleIgnoreCase("java");
        Assertions.assertThat(optionalBook.isPresent()).isFalse();
    }

}
