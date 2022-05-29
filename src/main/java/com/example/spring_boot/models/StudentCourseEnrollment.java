package com.example.spring_boot.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentCourseEnrollment {

    @NotNull(message = "Field listStudent must not be empty")
    List<Long> listStudent;

    @NotNull(message = "Field course must not be empty")
    String course;
}
