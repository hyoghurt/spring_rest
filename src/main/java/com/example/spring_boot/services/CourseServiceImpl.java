package com.example.spring_boot.services;

import com.example.spring_boot.exceptions.EntityNotFoundException;
import com.example.spring_boot.mappers.StudentCourseMapper;
import com.example.spring_boot.models.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{
    private final StudentCourseMapper studentCourseMapper;

    @Autowired
    public CourseServiceImpl(StudentCourseMapper studentCourseMapper) {
        this.studentCourseMapper = studentCourseMapper;
    }

    @Override
    public List<Course> getAll() {
        return studentCourseMapper.selectAllCourse();
    }

    @Override
    public Course add(Course course) {
        if (studentCourseMapper.selectByNameCourse(course.getName()) == null) {
            studentCourseMapper.insertCourse(course);
        }
        return course;
    }

    @Override
    public Course getByName(String name) {
        Course courseSelect = studentCourseMapper.selectByNameCourse(name);
        if (courseSelect == null) {
            throw new EntityNotFoundException("Course not found: " + name);
        }
        return courseSelect;
    }

    @Override
    public Course update(String name, Course course) {
        if (studentCourseMapper.selectByNameCourse(name) == null) {
            add(course);
        } else {
            studentCourseMapper.updateCourse(course);
        }
        return course;
    }

    @Override
    public void deleteByName(String name) {
        getByName(name);
        studentCourseMapper.deleteByNameCourse(name);
    }
}
