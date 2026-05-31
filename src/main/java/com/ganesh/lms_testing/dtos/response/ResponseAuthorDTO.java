package com.ganesh.lms_testing.dtos.response;

import com.ganesh.lms_testing.models.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseAuthorDTO {
    private Long id;
    private String name;
    private String email;
    private Gender gender;
    private Set<SimpleBookDTO>  books;
}
