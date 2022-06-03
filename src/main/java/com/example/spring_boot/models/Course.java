package com.example.spring_boot.models;

import lombok.*;

import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @NotNull(message = "Field name must not be empty")
    private String name;

    @NotNull(message = "Field description must not be empty")
    private String description;

    @NotNull(message = "Field requiredGrade must not be empty")
    private Integer requiredGrade;
}
