package com.example.spring_boot.controllers;

import com.example.spring_boot.models.Course;
import com.example.spring_boot.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final StudentService studentService;

    @Autowired
    public CourseController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Course> allCourse() {
        return studentService.getAllCourse();
    }

    @GetMapping("/{name}")
    public Course getCourse(@PathVariable String name) {
        return studentService.getByNameCourse(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course newCourse(@RequestBody @Validated Course course) {
        return studentService.saveCourse(course);
    }

    @PutMapping("/{name}")
    public Course updateCourse(@PathVariable String name, @RequestBody @Validated Course course) {
        return studentService.updateCourse(name, course);
    }

    @DeleteMapping("/{name}")
    public void deleteCourse(@PathVariable String name) {
        studentService.deleteByNameCourse(name);
    }
}
