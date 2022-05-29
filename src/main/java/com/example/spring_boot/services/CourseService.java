package com.example.spring_boot.services;

import com.example.spring_boot.models.Course;

import java.util.List;

public interface CourseService {
    Course add(Course course);
    Course getByName(String name);
    Course update(String name, Course course);
    void deleteByName(String name);
    List<Course> getAll();
}
