package com.example.spring_boot.mappers;

import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.StudentResponse;
import com.example.spring_boot.models.StudentRequest;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface StudentMapper {

    Course selectCourseMaxAgeAvg();

    //__STUDENT________________________________
    List<StudentResponse> selectAllStudent();

    StudentResponse selectByIdStudent(Long id);

    void insertStudent(StudentRequest student);

    void updateStudent(StudentRequest studentRequest);

    void deleteByIdStudent(Long id);

    //__COURSE________________________________
    List<Course> selectAllCourse();

    Course selectByNameCourse(String name);

    void insertCourse(Course course);

    void updateCourse(Course course);

    void deleteByNameCourse(String name);
}
