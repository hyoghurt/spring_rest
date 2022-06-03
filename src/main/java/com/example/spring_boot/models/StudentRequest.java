package com.example.spring_boot.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentRequest {

    public StudentRequest(StudentResponse studentResponse) {
        this.id = studentResponse.getId();
        this.name = studentResponse.getName();
        this.age = studentResponse.getAge();
        this.timeFrom = studentResponse.getTimeFrom();
        this.timeTo = studentResponse.getTimeTo();
        this.grade = studentResponse.getGrade();
        this.course = studentResponse.getCourse().getName();
    }

    @Null
    private Long id;

    @NotEmpty(message = "Field name must not be empty")
    private String name;

    @NotNull(message = "Field age must not be empty")
    private Integer age;

    @NotNull(message = "Field timeFrom must not be empty")
    private Integer timeFrom;

    @NotNull(message = "Field timeTo must not be empty")
    private Integer timeTo;

    @NotEmpty(message = "Field course must not be empty")
    private String course;

    @NotNull(message = "Field grade must not be empty")
    private Integer grade;
}
