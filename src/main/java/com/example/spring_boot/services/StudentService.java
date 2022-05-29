package com.example.spring_boot.services;

import com.example.spring_boot.models.Student;
import com.example.spring_boot.models.StudentCourseEnrollment;

import java.util.List;

public interface StudentService {
    Student add(Student student);
    Student getById(Long id);
    Student update(Long id, Student student);
    void deleteById(Long id);
    List<Student> getAll();
    void studentCourseEnrollment(StudentCourseEnrollment sce);
}
