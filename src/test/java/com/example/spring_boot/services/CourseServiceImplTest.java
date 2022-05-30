package com.example.spring_boot.services;

import com.example.spring_boot.exceptions.EntityNotFoundException;
import com.example.spring_boot.mappers.StudentCourseMapper;
import com.example.spring_boot.models.Course;
import com.example.spring_boot.util.CourseCache;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class CourseServiceImplTest {

    private CourseCache courseCache;
    private StudentCourseMapper studentCourseMapper;
    private CourseService courseService;
    private Course course;
    private Course actualCourse;

    @BeforeEach
    void setUp() {
        studentCourseMapper = Mockito.mock(StudentCourseMapper.class);
        courseCache = new CourseCache();
        courseService = new CourseServiceImpl(studentCourseMapper, courseCache);

        course = new Course("java", "java course", 3);
        actualCourse = new Course("java", "java course", 3);
    }

    //_______TEST_CASH___________________
    @Test
    void addNoCache() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(null);

        Course exceptionCourse = courseService.add(course);

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).insertCourse(course);

        assertEquals(exceptionCourse, actualCourse);
        assertEquals(courseCache.get(exceptionCourse.getName()), actualCourse);
        log.info("size cash:" + courseCache.size());
    }

    @Test
    void addHaveCache() {
        courseCache.add(course);

        Course exceptionCourse = courseService.add(course);

        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(exceptionCourse, actualCourse);
        assertEquals(courseCache.get(exceptionCourse.getName()), actualCourse);
    }

    @Test
    void getByNameHaveCache() {
        courseCache.add(course);

        Course exceptionCourse = courseService.getByName(course.getName());

        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void getByNameNoCache() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);
        assertEquals(courseCache.size(), 0);

        Course exceptionCourse = courseService.getByName(course.getName());

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).selectByNameCourse(course.getName());
        Mockito.verifyNoMoreInteractions(studentCourseMapper);

        assertEquals(courseCache.size(), 1);
        assertEquals(courseCache.get(course.getName()), actualCourse);
        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void updateHaveCache() {
        courseCache.add(course);

        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);

        Course updateCourse = new Course();
        updateCourse.setName(course.getName());
        updateCourse.setDescription(course.getDescription());
        updateCourse.setRequiredGrade(course.getRequiredGrade());
        updateCourse.setDescription("update desc");

        courseService.update(course.getName(), updateCourse);

        assertEquals(courseCache.get(course.getName()), updateCourse);
    }

    @Test
    void updateNoCache() {
        assertEquals(courseCache.size(), 0);
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);

        Course updateCourse = new Course();
        updateCourse.setName(course.getName());
        updateCourse.setDescription(course.getDescription());
        updateCourse.setRequiredGrade(course.getRequiredGrade());
        updateCourse.setDescription("update desc");

        courseService.update(course.getName(), updateCourse);

        assertEquals(courseCache.get(course.getName()), updateCourse);
    }

    @Test
    void deleteByNameHaveCache() {
        courseCache.add(course);

        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);

        courseService.deleteByName(course.getName());

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).deleteByNameCourse(course.getName());

        assertNull(courseCache.get(course.getName()));
        assertEquals(courseCache.size(), 0);
    }

    @Test
    void deleteByNameNoCache() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);

        courseService.deleteByName(course.getName());

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).deleteByNameCourse(course.getName());

        assertNull(courseCache.get(course.getName()));
        assertEquals(courseCache.size(), 0);
    }

    @Test
    void getAll() {
        List<Course> list = new ArrayList<>();
        list.add(new Course("java", "course java", 4));
        list.add(new Course("kek", "course kek", 4));

        List<Course> actualList = new ArrayList<>(list);

        Mockito.when(studentCourseMapper.selectAllCourse()).thenReturn(list);

        List<Course> expectedList = courseService.getAll();

        assertEquals(expectedList, actualList);
    }

    @Test
    void addSuccess() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(null);

        Course exceptionCourse = courseService.add(course);

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).insertCourse(course);

        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void addFindCourse() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);

        Course exceptionCourse = courseService.add(course);

        Mockito.verify(studentCourseMapper,
                Mockito.times(0)).insertCourse(course);

        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void getByNameSuccess() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);

        Course exceptionCourse = courseService.getByName(course.getName());

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).selectByNameCourse(course.getName());

        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void getByNameException() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> courseService.getByName(course.getName()));

        assertEquals(ex.getMessage(), "Course not found");
    }

    @Test
    void updateSuccess() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);

        Course exceptionCourse = courseService.update(course.getName(), course);

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).updateCourse(course);

        assertEquals(exceptionCourse, actualCourse);
    }

    @Test
    void updateException() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> courseService.update(course.getName(), course));

        Mockito.verify(studentCourseMapper,
                Mockito.times(0)).updateCourse(course);

        assertEquals(ex.getMessage(), "Course not found");
    }

    @Test
    void deleteByNameSuccess() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(course);

        courseService.deleteByName(course.getName());

        Mockito.verify(studentCourseMapper,
                Mockito.times(1)).deleteByNameCourse(course.getName());
    }

    @Test
    void deleteByNameException() {
        Mockito.when(studentCourseMapper.selectByNameCourse(course.getName())).thenReturn(null);

        Exception ex = assertThrows(EntityNotFoundException.class,
                () -> courseService.deleteByName(course.getName()));

        Mockito.verify(studentCourseMapper,
                Mockito.times(0)).deleteByNameCourse(course.getName());

        assertEquals(ex.getMessage(), "Course not found");
    }
}