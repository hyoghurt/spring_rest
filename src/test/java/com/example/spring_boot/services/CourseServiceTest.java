package com.example.spring_boot.services;

import com.example.spring_boot.exceptions.EntityNotFoundException;
import com.example.spring_boot.mappers.StudentMapper;
import com.example.spring_boot.models.Course;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CourseServiceTest {

    private StudentMapper studentMapper;
    private StudentService studentService;
    private Course course;
    private Course actualCourse;

    @BeforeEach
    void setUp() {
        studentMapper = Mockito.mock(StudentMapper.class);
        studentService = new StudentServiceImpl(studentMapper);

        course = new Course("java", "java course", 3);
        actualCourse = new Course("java", "java course", 3);
    }

    @Test
    void testCache() {
        Course course1 = new Course("java1", "java course", 3);
        Course course2 = new Course("java2", "java course", 3);
        Course course3 = new Course("java3", "java course", 3);

        Mockito.when(studentMapper.selectByNameCourse(course1.getName())).thenReturn(course1);

        log.info("select 1");
        studentService.getByNameCourse(course1.getName());
        log.info("select 2");
        studentService.getByNameCourse(course1.getName());
        log.info("select 3");
        studentService.getByNameCourse(course1.getName());
        log.info("select fin");
    }

    @Test
    void getAll() {
        List<Course> list = new ArrayList<>();
        list.add(new Course("java", "course java", 4));
        list.add(new Course("kek", "course kek", 4));

        List<Course> actualList = new ArrayList<>(list);

        Mockito.when(studentMapper.selectAllCourse()).thenReturn(list);

        List<Course> expectedList = studentService.getAllCourse();

        assertEquals(expectedList, actualList);
    }

    @Test
    void addSuccess() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(null);

        Course exceptionCourse = studentService.saveCourse(course);

        Mockito.verify(studentMapper,
                Mockito.times(1)).insertCourse(course);

        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void addFindCourse() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(course);

        Course exceptionCourse = studentService.saveCourse(course);

        Mockito.verify(studentMapper,
                Mockito.times(0)).insertCourse(course);

        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void getByNameSuccess() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(course);

        Course exceptionCourse = studentService.getByNameCourse(course.getName());

        Mockito.verify(studentMapper,
                Mockito.times(1)).selectByNameCourse(course.getName());

        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void getByNameException() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.getByNameCourse(course.getName()));

        assertEquals(ex.getMessage(), "Course not found");
    }

    @Test
    void updateSuccess() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(course);

        Course exceptionCourse = studentService.updateCourse(course.getName(), course);

        Mockito.verify(studentMapper,
                Mockito.times(1)).updateCourse(course);

        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void updateException() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> studentService.updateCourse(course.getName(), course));

        Mockito.verify(studentMapper,
                Mockito.times(0)).updateCourse(course);

        assertEquals(ex.getMessage(), "Course not found");
    }

    @Test
    void deleteByNameSuccess() {
        Mockito.when(studentMapper.selectByNameCourse(course.getName())).thenReturn(course);

        studentService.deleteByNameCourse(course.getName());

        Mockito.verify(studentMapper,
                Mockito.times(1)).deleteByNameCourse(course.getName());
    }
}