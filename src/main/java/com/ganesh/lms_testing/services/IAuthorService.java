package com.ganesh.lms_testing.services;

import com.ganesh.lms_testing.dtos.request.RequestAuthorDTO;
import com.ganesh.lms_testing.dtos.response.ResponseAuthorDTO;
import com.ganesh.lms_testing.dtos.response.ResponseBookDTO;

public interface IAuthorService {
    ResponseAuthorDTO createAuthor(RequestAuthorDTO  requestAuthorDTO);

    ResponseAuthorDTO getAuthorById(Long id);

    ResponseAuthorDTO updateAuthor(RequestAuthorDTO requestAuthorDTO, String email);

    String deleteAuthorById(Long id);

    ResponseAuthorDTO findAuthorByName(String name);
}
