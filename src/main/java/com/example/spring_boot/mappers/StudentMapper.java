package com.example.spring_boot.mappers;

import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.Student;
import org.apache.ibatis.annotations.*;

@Mapper
public interface StudentMapper {

    public Course selectCourseMaxAgeAvg();

    public Student selectByIdStudent(Long id);

    public void insertStudent(Student student);

    public void deleteByIdStudent(Long id);

    public void updateStudent(Student student);

    public Course selectByNameCourse(String name);

    public void deleteByNameCourse(String name);

    public void insertCourse(Course course);

    public void updateCourse(Course course);
}
