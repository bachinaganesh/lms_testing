package com.ganesh.lms_testing.controller;

import com.ganesh.lms_testing.dtos.request.RequestBookDTO;
import com.ganesh.lms_testing.dtos.response.ResponseBookDTO;
import com.ganesh.lms_testing.services.IBookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final IBookService  bookService;

    @PostMapping
    public ResponseEntity<ResponseBookDTO> createBook(@Valid @RequestBody RequestBookDTO bookDTO, @RequestParam Long authorId) {
        ResponseBookDTO createdBook = bookService.createBook(bookDTO, authorId);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseBookDTO> getBookById(@PathVariable Long id) {
        ResponseBookDTO responseBookDTO = bookService.getBookById(id);
        return new ResponseEntity<>(responseBookDTO, HttpStatus.OK);
    }

    @PutMapping("/{bookId}")
    public ResponseEntity<ResponseBookDTO> updateBookById(@PathVariable Long bookId, @Valid @RequestBody RequestBookDTO bookDTO) {
        ResponseBookDTO responseBookDTO = this.bookService.updateBookById(bookDTO, bookId);
        return new ResponseEntity<>(responseBookDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookById(@PathVariable Long id) {
        String response = bookService.deleteBookById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseBookDTO> getBookByTitle(@RequestParam String title) {
        ResponseBookDTO responseBookDTO = bookService.getBookByTitle(title);
        return new ResponseEntity<>(responseBookDTO, HttpStatus.OK);
    }

}
