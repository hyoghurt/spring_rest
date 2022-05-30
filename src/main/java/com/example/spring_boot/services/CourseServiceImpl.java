package com.example.spring_boot.services;

import com.example.spring_boot.exceptions.EntityNotFoundException;
import com.example.spring_boot.mappers.StudentCourseMapper;
import com.example.spring_boot.models.Course;
import com.example.spring_boot.util.CourseCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService{
    private final StudentCourseMapper studentCourseMapper;
    private final CourseCache courseCache;

    @Autowired
    public CourseServiceImpl(StudentCourseMapper studentCourseMapper, CourseCache courseCache) {
        this.studentCourseMapper = studentCourseMapper;
        this.courseCache = courseCache;
    }

    @Override
    public List<Course> getAll() {
        return studentCourseMapper.selectAllCourse();
    }

    @Override
    @Transactional
    public Course add(Course course) {
        Course courseGet = courseCache.get(course.getName());
        if (courseGet != null) return courseGet;

        if (studentCourseMapper.selectByNameCourse(course.getName()) == null) {
            studentCourseMapper.insertCourse(course);
        }
        courseCache.add(course);
        return course;
    }

    @Override
    public Course getByName(String name) {
        Course courseGet = courseCache.get(name);
        if (courseGet != null) return courseGet;

        Course courseSelect = studentCourseMapper.selectByNameCourse(name);
        if (courseSelect == null) {
            throw new EntityNotFoundException("Course not found");
        }
        courseCache.add(courseSelect);
        return courseSelect;
    }

    @Override
    @Transactional
    public Course update(String name, Course course) {
        getByName(name);
        course.setName(name);
        studentCourseMapper.updateCourse(course);
        courseCache.replace(name, course);
        return course;
    }

    @Override
    @Transactional
    public void deleteByName(String name) {
        getByName(name);
        studentCourseMapper.deleteByNameCourse(name);
        courseCache.remove(name);
    }
}
