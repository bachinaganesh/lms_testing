package com.ganesh.lms_testing.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SimpleBookDTO {
    private Long id;
    private String title;
    private Double price;
}
