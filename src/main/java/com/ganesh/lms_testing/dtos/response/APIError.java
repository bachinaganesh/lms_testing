package com.ganesh.lms_testing.dtos.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class APIError {
    private String message;
    private String path;
    private LocalDateTime timestamp;
    private HttpStatus status;
}
