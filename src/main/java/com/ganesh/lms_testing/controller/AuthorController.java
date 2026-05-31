package com.ganesh.lms_testing.controller;

import com.ganesh.lms_testing.dtos.request.RequestAuthorDTO;
import com.ganesh.lms_testing.dtos.response.ResponseAuthorDTO;
import com.ganesh.lms_testing.dtos.response.ResponseBookDTO;
import com.ganesh.lms_testing.services.IAuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final IAuthorService authorService;

    @PostMapping
    public ResponseEntity<ResponseAuthorDTO> createAuthor(@Valid @RequestBody RequestAuthorDTO authorDTO) {
        ResponseAuthorDTO responseAuthorDTO = authorService.createAuthor(authorDTO);
        return new ResponseEntity<>(responseAuthorDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAuthorDTO> getAuthorById(@PathVariable Long id) {
        ResponseAuthorDTO responseAuthorDTO = authorService.getAuthorById(id);
        return new ResponseEntity<>(responseAuthorDTO, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseAuthorDTO> updateAuthor(@Valid @RequestBody RequestAuthorDTO authorDTO, @RequestParam String email) {
        ResponseAuthorDTO responseAuthorDTO = authorService.updateAuthor(authorDTO, email);
        return new ResponseEntity<>(responseAuthorDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthorById(@PathVariable Long id) {
        String response =  authorService.deleteAuthorById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseAuthorDTO> findAuthorByName(@RequestParam String name) {
        ResponseAuthorDTO responseAuthorDTO = this.authorService.findAuthorByName(name);
        return new ResponseEntity<>(responseAuthorDTO, HttpStatus.OK);
    }
}
