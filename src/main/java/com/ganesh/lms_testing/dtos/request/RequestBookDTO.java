package com.ganesh.lms_testing.dtos.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestBookDTO {
    @NotBlank(message = "Title is required")
    private String title;
    @Min(value = 200, message = "Price must be at least 200")
    @Max(value = 1000, message = "Price must be at most 1000")
    @NotNull(message = "Price is required")
    private Double price;
}
