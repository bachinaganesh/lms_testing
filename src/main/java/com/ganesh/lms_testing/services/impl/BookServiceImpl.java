package com.ganesh.lms_testing.services.impl;

import com.ganesh.lms_testing.dtos.request.RequestBookDTO;
import com.ganesh.lms_testing.dtos.response.ResponseBookDTO;
import com.ganesh.lms_testing.exceptions.ResourceNotFoundException;
import com.ganesh.lms_testing.models.Author;
import com.ganesh.lms_testing.models.Book;
import com.ganesh.lms_testing.repository.AuthorRepository;
import com.ganesh.lms_testing.repository.BookRepository;
import com.ganesh.lms_testing.services.IBookService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseBookDTO createBook(RequestBookDTO requestBookDTO, Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + id));

        Book book = this.modelMapper.map(requestBookDTO, Book.class);
        book.setAuthor(author);
        Book savedBook = this.bookRepository.save(book);
        return  modelMapper.map(savedBook, ResponseBookDTO.class);
    }

    @Override
    public ResponseBookDTO getBookById(Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        return modelMapper.map(book, ResponseBookDTO.class);
    }

    @Override
    public ResponseBookDTO updateBookById(RequestBookDTO requestBookDTO, Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        Book needToSave = this.modelMapper.map(requestBookDTO, Book.class);
        needToSave.setAuthor(book.getAuthor());
        needToSave.setId(book.getId());
        Book savedBook = this.bookRepository.save(needToSave);
        return modelMapper.map(savedBook, ResponseBookDTO.class);
    }

    @Override
    public String deleteBookById(Long id) {
        boolean exists = this.bookRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Book not found with id " + id);
        }
        this.bookRepository.deleteById(id);
        return "Book with id " + id + " has been deleted";
    }

    @Override
    public ResponseBookDTO getBookByTitle(String title) {
        Book book = this.bookRepository.findByTitleIgnoreCase(title).orElseThrow(() -> new ResourceNotFoundException("Book not found with title " + title));
        return modelMapper.map(book, ResponseBookDTO.class);
    }
}
