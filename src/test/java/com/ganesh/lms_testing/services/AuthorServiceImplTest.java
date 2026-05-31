package com.ganesh.lms_testing.services;

import com.ganesh.lms_testing.dtos.request.RequestAuthorDTO;
import com.ganesh.lms_testing.dtos.response.ResponseAuthorDTO;
import com.ganesh.lms_testing.exceptions.NotUpdateableException;
import com.ganesh.lms_testing.exceptions.ResourceAlreadyExistException;
import com.ganesh.lms_testing.exceptions.ResourceNotFoundException;
import com.ganesh.lms_testing.models.Author;
import com.ganesh.lms_testing.models.enums.Gender;
import com.ganesh.lms_testing.repository.AuthorRepository;
import com.ganesh.lms_testing.services.impl.AuthorServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorServiceImpl authorService;

    @Spy
    private ModelMapper modelMapper;

    private RequestAuthorDTO  requestAuthorDTO;
    private Author author;

    @BeforeEach
    void setUp() {
        requestAuthorDTO = RequestAuthorDTO.builder()
                .name("Ganesh")
                .email("ganesh@gmail.com")
                .gender(Gender.MALE)
                .build();

        author = Author.builder()
                .id(1L)
                .name("Ganesh")
                .email("ganesh@gmail.com")
                .gender(Gender.MALE)
                .build();
    }

    @Test
    void testCreateAuthor_WhenNewAuthor_ShouldReturnAuthor() {
        when(authorRepository.existsByEmail(anyString())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        ResponseAuthorDTO responseAuthorDTO = authorService.createAuthor(requestAuthorDTO);
        Assertions.assertThat(responseAuthorDTO).isNotNull();
        Assertions.assertThat(responseAuthorDTO.getId()).isEqualTo(author.getId());

        verify(authorRepository, times(1)).existsByEmail(anyString());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testCreateAuthor_WhenExistingAuthor_ShouldReturnException() {
        when(authorRepository.existsByEmail(anyString())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> authorService.createAuthor(requestAuthorDTO)).isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Author already exists with email: " + requestAuthorDTO.getEmail());

        verify(authorRepository, times(1)).existsByEmail(anyString());
        verify(authorRepository, times(0)).save(any(Author.class));
    }

    @Test
    void testGetAuthorById_WhenExistingAuthor_ShouldReturnAuthor() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
        ResponseAuthorDTO responseAuthorDTO = authorService.getAuthorById(1L);
        Assertions.assertThat(responseAuthorDTO).isNotNull();
        Assertions.assertThat(responseAuthorDTO.getId()).isEqualTo(author.getId());
        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetAuthorById_WhenAuthorNotFound_ShouldReturnException() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(() -> authorService.getAuthorById(1L)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with id: " + 1L);

        verify(authorRepository, times(1)).findById(anyLong());
    }

    @Test
    void testDeleteAuthorById_WhenExistingAuthor_ShouldDeleteAuthor() {
        when(authorRepository.existsById(anyLong())).thenReturn(true);
        doNothing().when(authorRepository).deleteById(anyLong());

        String response =  authorService.deleteAuthorById(1L);
        Assertions.assertThat(response).isNotNull();
        Assertions.assertThat(response).isEqualTo("Author with id " + 1L + " has been deleted");

        verify(authorRepository, times(1)).existsById(anyLong());
        verify(authorRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteAuthorById_WhenAuthorNotFound_ShouldReturnException() {
        when(authorRepository.existsById(anyLong())).thenReturn(false);
        Assertions.assertThatThrownBy(() -> authorService.deleteAuthorById(1L)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with id: " + 1L);
        verify(authorRepository, times(1)).existsById(anyLong());
        verify(authorRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void testFindAuthorByName_WhenExistingAuthor_ShouldReturnAuthor() {
        when(authorRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.of(author));
        ResponseAuthorDTO responseAuthorDTO = authorService.findAuthorByName("Ganesh");
        Assertions.assertThat(responseAuthorDTO).isNotNull();

        verify(authorRepository, times(1)).findByNameIgnoreCase(anyString());
    }

    @Test
    void testFindAuthorByName_WhenAuthorNotFound_ShouldReturnException() {
        when(authorRepository.findByNameIgnoreCase(anyString())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> authorService.findAuthorByName("Ganesh")).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with name: " + "Ganesh");
    }

    @Test
    void testUpdateAuthor_WhenExistingAuthor_ShouldReturnUpdatedAuthor() {
        requestAuthorDTO = RequestAuthorDTO.builder()
                        .name("Lakshmi")
                        .email("ganesh@gmail.com")
                        .gender(Gender.FEMALE)
                        .build();
        Author updatedAuthor = modelMapper.map(requestAuthorDTO, Author.class);
        updatedAuthor.setId(author.getId());
        when(authorRepository.findByEmail(anyString())).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);

        ResponseAuthorDTO responseAuthorDTO = authorService.updateAuthor(requestAuthorDTO, "ganesh@gmail.com");

        Assertions.assertThat(responseAuthorDTO).isNotNull();
        Assertions.assertThat(responseAuthorDTO.getName()).isEqualTo(requestAuthorDTO.getName());
        Assertions.assertThat(responseAuthorDTO.getGender()).isEqualTo(requestAuthorDTO.getGender());

        verify(authorRepository, times(1)).findByEmail(anyString());
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void testUpdateAuthor_WhenAuthorNotFound_ShouldReturnException() {
        requestAuthorDTO = RequestAuthorDTO.builder()
                .name("Lakshmi")
                .email("ganesh@gmail.com")
                .gender(Gender.FEMALE)
                .build();
        when(authorRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(()-> authorService.updateAuthor(requestAuthorDTO, "ganesh@gmail.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with email: " + "ganesh@gmail.com");

        verify(authorRepository, times(1)).findByEmail(anyString());
        verify(authorRepository, times(0)).save(any(Author.class));
    }

    @Test
    void testUpdateAuthor_WhenAuthorEmailChanges_ShouldThrowNotUpdateException() {
        requestAuthorDTO = RequestAuthorDTO.builder()
                .name("Lakshmi")
                .email("suresh@gmail.com")
                .gender(Gender.FEMALE)
                .build();

        when(authorRepository.findByEmail(anyString())).thenReturn(Optional.of(author));
        Assertions.assertThatThrownBy(() -> authorService.updateAuthor(requestAuthorDTO, "ganesh@gmail.com"))
                .isInstanceOf(NotUpdateableException.class)
                .hasMessage("Author email cannot be updated only name and gender can be updatable");

        verify(authorRepository, times(1)).findByEmail(anyString());
        verify(authorRepository, times(0)).save(any(Author.class));
    }
}
