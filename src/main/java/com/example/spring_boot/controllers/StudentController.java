package com.example.spring_boot.controllers;

import com.example.spring_boot.models.StudentResponse;
import com.example.spring_boot.models.StudentCourseEnrollment;
import com.example.spring_boot.models.StudentRequest;
import com.example.spring_boot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<StudentResponse> allStudent() {
        return studentService.getAllStudent();
    }

    @GetMapping("/{id}")
    public StudentResponse getStudent(@PathVariable Long id) {
        return studentService.getByIdStudent(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StudentResponse newStudent(@RequestBody @Validated StudentRequest student) {
        return studentService.saveStudent(student);
    }

    @PutMapping("/{id}")
    public StudentResponse updateStudent(@PathVariable Long id,
                                         @RequestBody @Validated StudentRequest studentRequest) {
        return studentService.updateStudent(id, studentRequest);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteByIdStudent(id);
    }

    @PostMapping("/course_enrollment")
    public void studentCourseEnrollment(@RequestBody @Validated StudentCourseEnrollment sce) {
        studentService.studentCourseEnrollment(sce);
    }
}
