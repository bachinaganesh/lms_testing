package com.ganesh.lms_testing.services;

import com.ganesh.lms_testing.dtos.request.RequestBookDTO;
import com.ganesh.lms_testing.dtos.response.ResponseBookDTO;

public interface IBookService {
    ResponseBookDTO createBook(RequestBookDTO requestBookDTO, Long id);
    ResponseBookDTO getBookById(Long id);

    ResponseBookDTO updateBookById(RequestBookDTO requestBookDTO, Long id);

    String deleteBookById(Long id);

    ResponseBookDTO getBookByTitle(String title);
}
