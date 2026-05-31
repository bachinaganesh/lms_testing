package com.ganesh.lms_testing.repository;

import com.ganesh.lms_testing.TestcontainersConfiguration;
import com.ganesh.lms_testing.models.Author;
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
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    private Author author;

    @BeforeEach
    public void setUp() {
        author = Author.builder()
                .name("Ganesh")
                .email("ganesh@gmail.com")
                .gender(Gender.MALE)
                .build();
    }

    @Test
    public void testExistByEmail_WhenAuthorExists_ReturnsTrue() {
        authorRepository.save(author);
        boolean exists = authorRepository.existsByEmail(author.getEmail());
        Assertions.assertThat(exists).isTrue();
    }

    @Test
    public void testExistByEmail_WhenAuthorDoesNotExist_ReturnsFalse() {
        boolean exists = authorRepository.existsByEmail(author.getEmail());
        Assertions.assertThat(exists).isFalse();
    }

    @Test
    public void testFindByEmail_WhenAuthorExists_ReturnsAuthor() {
        authorRepository.save(author);
        Optional<Author> found = authorRepository.findByEmail(author.getEmail());
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.get()).isEqualTo(author);
    }

    @Test
    public void testFindByEmail_WhenAuthorDoesNotExist_ReturnsNull() {
        Optional<Author> found = authorRepository.findByEmail(author.getEmail());
        Assertions.assertThat(found).isEmpty();
    }

    @Test
    public void testFindByName_WhenAuthorExists_ReturnsAuthor() {
        authorRepository.save(author);
        Optional<Author> found = authorRepository.findByNameIgnoreCase(author.getName());
        Assertions.assertThat(found).isNotNull();
        Assertions.assertThat(found.get()).isEqualTo(author);
    }

    @Test
    public void testFindByName_WhenAuthorDoesNotExist_ReturnsNull() {
        Optional<Author> found = authorRepository.findByNameIgnoreCase(author.getName());
        Assertions.assertThat(found).isEmpty();
    }
}
