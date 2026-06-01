package com.ganesh.lms_testing.services;

import com.ganesh.lms_testing.dtos.request.RequestBookDTO;
import com.ganesh.lms_testing.dtos.response.ResponseBookDTO;
import com.ganesh.lms_testing.exceptions.ResourceNotFoundException;
import com.ganesh.lms_testing.models.Author;
import com.ganesh.lms_testing.models.Book;
import com.ganesh.lms_testing.models.enums.Gender;
import com.ganesh.lms_testing.repository.AuthorRepository;
import com.ganesh.lms_testing.repository.BookRepository;
import com.ganesh.lms_testing.services.impl.BookServiceImpl;
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
public class BookServiceImplTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Spy
    private ModelMapper modelMapper;

    private RequestBookDTO requestBookDTO;
    private Author author;
//    private ResponseBookDTO responseBookDTO;
    private Book book;

    @BeforeEach
    void setUp() {
        requestBookDTO = RequestBookDTO.builder()
                .title("Python")
                .price(500.0)
                .build();

        author = Author.builder()
                .id(1L)
                .name("Ganesh")
                .email("ganesh@gmail.com")
                .gender(Gender.MALE)
                .build();

//        responseBookDTO = ResponseBookDTO.builder()
//                .title("Python")
//                .price(500.0)
//                .id(1L)
//                .author(this.modelMapper.map(author, SimpleAuthorDTO.class))
//                .build();

        book = Book.builder()
                .id(1L)
                .title("Python")
                .price(500.0)
                .author(author)
                .build();
    }

    @Test
    void testCreateBook_WhenAuthorIsNotExists_ShouldThrowException() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(()-> bookService.createBook(requestBookDTO, author.getId()))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Author not found with id " + author.getId());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    void testCreateBook_WhenAuthorExists_ShouldCreateBook() {
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        ResponseBookDTO responseBookDTO = bookService.createBook(requestBookDTO, author.getId());

        Assertions.assertThat(responseBookDTO).isNotNull();
        Assertions.assertThat(responseBookDTO.getTitle()).isEqualTo(book.getTitle());

        verify(authorRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testGetBookById_WhenBookNotExists_ShouldThrowException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(()-> bookService.getBookById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with id " + 1L);
        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetBookById_WhenBookExists_ShouldReturnBook() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        ResponseBookDTO responseBookDTO = bookService.getBookById(1L);

        Assertions.assertThat(responseBookDTO).isNotNull();
        Assertions.assertThat(responseBookDTO.getTitle()).isEqualTo(book.getTitle());

        verify(bookRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetBookByTitle_WhenBookNotExists_ShouldThrowException() {
        when(bookRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.empty());
        Assertions.assertThatThrownBy(()-> bookService.getBookByTitle("Python"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with title Python");
        verify(bookRepository, times(1)).findByTitleIgnoreCase(anyString());
    }

    @Test
    void testGetBookByTitle_WhenBookExists_ShouldReturnBook() {
        when(bookRepository.findByTitleIgnoreCase(anyString())).thenReturn(Optional.of(book));
        ResponseBookDTO responseBookDTO = bookService.getBookByTitle("Python");
        Assertions.assertThat(responseBookDTO).isNotNull();
        Assertions.assertThat(responseBookDTO.getTitle()).isEqualTo(book.getTitle());
        verify(bookRepository, times(1)).findByTitleIgnoreCase(anyString());
    }

    @Test
    void testDeleteBookById_WhenBookNotExists_ShouldThrowException() {
        when(bookRepository.existsById(anyLong())).thenReturn(false);

        Assertions.assertThatThrownBy(()-> bookService.deleteBookById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with id " + 1L);

        verify(bookRepository, times(1)).existsById(anyLong());
        verify(bookRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void testDeleteBookById_WhenBookExists_ShouldDeleteBook() {
        when(bookRepository.existsById(anyLong())).thenReturn(true);

        doNothing().when(bookRepository).deleteById(anyLong());

        String response = bookService.deleteBookById(1L);

        Assertions.assertThat(response).isNotNull();
        verify(bookRepository, times(1)).existsById(anyLong());
        verify(bookRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testUpdateBookById_WhenBookNotExists_ShouldThrowException() {
        when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> bookService.updateBookById(requestBookDTO, 1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Book not found with id " + 1L);

        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(0)).save(any(Book.class));
    }

    @Test
    void testUpdateBookById_WhenBookExists_ShouldUpdateBook() {
        RequestBookDTO updateRequestBookDTO = RequestBookDTO.builder()
                        .title("Kotlin")
                        .price(400.0)
                        .build();
        Book savedBook = Book.builder()
                        .id(1L)
                        .title("Kotlin")
                        .price(400.0)
                        .author(author)
                        .build();
        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(savedBook);

        ResponseBookDTO responseBookDTO = bookService.updateBookById(updateRequestBookDTO, 1L);
        Assertions.assertThat(responseBookDTO).isNotNull();
        Assertions.assertThat(responseBookDTO.getTitle()).isEqualTo(savedBook.getTitle());
        verify(bookRepository, times(1)).findById(anyLong());
        verify(bookRepository, times(1)).save(any(Book.class));

    }
}
