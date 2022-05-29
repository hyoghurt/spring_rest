package com.example.spring_boot.controllers;

import com.example.spring_boot.models.Course;
import com.example.spring_boot.services.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/course")
public class CourseController {
    private final CourseService courseService;

    public CourseController(CourseService studentService) {
        this.courseService = studentService;
    }

    @GetMapping
    public List<Course> allCourse() {
        return courseService.getAll();
    }

    @GetMapping("/{name}")
    public Course getCourse(@PathVariable String name) {
        return courseService.getByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Course newCourse(@RequestBody @Validated Course course) {
        return courseService.add(course);
    }

    @PutMapping("/{name}")
    public Course updateCourse(@PathVariable String name, @RequestBody @Validated Course course) {
        return courseService.update(name, course);
    }

    @DeleteMapping("/{name}")
    public void deleteCourse(@PathVariable String name) {
        courseService.deleteByName(name);
    }
}
