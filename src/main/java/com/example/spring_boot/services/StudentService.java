package com.example.spring_boot.services;

import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.StudentResponse;
import com.example.spring_boot.models.StudentCourseEnrollment;
import com.example.spring_boot.models.StudentRequest;

import java.util.List;

public interface StudentService {
    List<Course> getAllCourse();
    Course getByNameCourse(String name);
    Course saveCourse(Course course);
    Course updateCourse(String name, Course course);
    void deleteByNameCourse(String name);

    List<StudentResponse> getAllStudent();
    StudentResponse saveStudent(StudentRequest student);
    StudentResponse getByIdStudent(Long id);
    StudentResponse updateStudent(Long id, StudentRequest studentRequest);
    void deleteByIdStudent(Long id);
    void studentCourseEnrollment(StudentCourseEnrollment sce);
}
