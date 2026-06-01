package com.ganesh.lms_testing.dtos.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseBookDTO {
    private Long id;
    private String title;
    private Double price;
    private SimpleAuthorDTO author;
}
