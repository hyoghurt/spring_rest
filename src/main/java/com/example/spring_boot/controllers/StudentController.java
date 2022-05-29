package com.example.spring_boot.controllers;

import com.example.spring_boot.models.Student;
import com.example.spring_boot.models.StudentCourseEnrollment;
import com.example.spring_boot.services.StudentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> allStudent() {
        return studentService.getAll();
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getById(id);
    }

    @PostMapping
    public Student newStudent(@RequestBody @Validated Student student) {
        return studentService.add(student);
    }

    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody @Validated Student student) {
        return studentService.update(id, student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteById(id);
    }

    @PostMapping("/course")
    public void studentCourseEnrollment(@RequestBody @Validated StudentCourseEnrollment sce) {
        studentService.studentCourseEnrollment(sce);
    }
}
