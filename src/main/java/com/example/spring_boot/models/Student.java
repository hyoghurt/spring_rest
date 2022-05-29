package com.example.spring_boot.models;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Long id;

    @NotEmpty(message = "Field name must not be empty")
    private String name;

    @NotNull(message = "Field age must not be empty")
    private Integer age;

    @NotNull(message = "Field timeFrom must not be empty")
    private Integer timeFrom;

    @NotNull(message = "Field timeTo must not be empty")
    private Integer timeTo;

    @NotNull(message = "Field course must not be empty")
    private Course course;

    @NotNull(message = "Field grade must not be empty")
    private Integer grade;
}
