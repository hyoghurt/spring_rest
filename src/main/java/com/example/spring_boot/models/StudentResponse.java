package com.example.spring_boot.models;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    public StudentResponse(StudentRequest studentRequest, Course course) {
        this.id = studentRequest.getId();
        this.name = studentRequest.getName();
        this.age = studentRequest.getAge();
        this.timeFrom = studentRequest.getTimeFrom();
        this.timeTo = studentRequest.getTimeTo();
        this.grade = studentRequest.getGrade();
        this.course = course;
    }

    public StudentResponse clone() {
        StudentResponse student = new StudentResponse();
        student.id = this.getId();
        student.name = this.getName();
        student.age = this.getAge();
        student.timeFrom = this.getTimeFrom();
        student.timeTo = this.getTimeTo();
        student.grade = this.getGrade();
        student.course = this.getCourse();
        return student;
    }

    private Long id;
    private String name;
    private Integer age;
    private Integer timeFrom;
    private Integer timeTo;
    private Integer grade;
    private Course course;
}
