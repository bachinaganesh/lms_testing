package com.ganesh.lms_testing.services.impl;

import com.ganesh.lms_testing.dtos.request.RequestAuthorDTO;
import com.ganesh.lms_testing.dtos.response.ResponseAuthorDTO;
import com.ganesh.lms_testing.exceptions.NotUpdateableException;
import com.ganesh.lms_testing.exceptions.ResourceAlreadyExistException;
import com.ganesh.lms_testing.exceptions.ResourceNotFoundException;
import com.ganesh.lms_testing.models.Author;
import com.ganesh.lms_testing.repository.AuthorRepository;
import com.ganesh.lms_testing.services.IAuthorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements IAuthorService {

    private final AuthorRepository authorRepository;
    private final ModelMapper modelMapper;

    @Override
    public ResponseAuthorDTO createAuthor(RequestAuthorDTO requestAuthorDTO) {
        boolean isExists = authorRepository.existsByEmail(requestAuthorDTO.getEmail());
        if(isExists){
            throw new ResourceAlreadyExistException("Author already exists with email: " + requestAuthorDTO.getEmail());
        }
        Author author = modelMapper.map(requestAuthorDTO, Author.class);
        Author savedAuthor = authorRepository.save(author);
        return modelMapper.map(savedAuthor, ResponseAuthorDTO.class);
    }

    @Override
    public ResponseAuthorDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return modelMapper.map(author, ResponseAuthorDTO.class);
    }

    @Override
    public ResponseAuthorDTO updateAuthor(RequestAuthorDTO requestAuthorDTO, String email) {
        Author author = this.authorRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("Author not found with email: " + email));
//        If email not same then throw an exception
        if(!requestAuthorDTO.getEmail().equals(author.getEmail())){
            throw new NotUpdateableException("Author email cannot be updated only name and gender can be updatable");
        }
        Author updatedAuthor = modelMapper.map(requestAuthorDTO, Author.class);
        updatedAuthor.setId(author.getId());

        Author savedAuthor = authorRepository.save(updatedAuthor);
        return modelMapper.map(savedAuthor, ResponseAuthorDTO.class);
    }

    @Override
    public String deleteAuthorById(Long id) {
        boolean exists = this.authorRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }

        this.authorRepository.deleteById(id);
        return "Author with id " + id + " has been deleted";
    }

    @Override
    public ResponseAuthorDTO findAuthorByName(String name) {
        Author author = this.authorRepository.findByNameIgnoreCase(name).orElseThrow(() -> new ResourceNotFoundException("Author not found with name: " + name));
        return modelMapper.map(author, ResponseAuthorDTO.class);
    }
}
