package com.example.spring_boot.mappers;

import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StudentCourseMapperTest {

    @Autowired
    StudentCourseMapper studentCourseMapper;

    @Test
    public void myTest() {
        List<Student> list = studentCourseMapper.selectAllStudent();
        list.forEach(System.out::println);

        List<Course> list1 = studentCourseMapper.selectAllCourse();
        list1.forEach(System.out::println);
    }

    @Test
    public void selectMaxAgeAvgCourseTest() {
        Course course = studentCourseMapper.selectCourseMaxAgeAvg();
        if (course != null)
            System.out.println(course.toString());
    }

    @Test
    public void crudStudentTest() {
        Course course = new Course("c++", "best course c++", 4);
        Course courseSelect;
        Student student = new Student(null, "Lik", 12, 15, 18, course, 3);
        Student studentSelect;

        studentCourseMapper.insertCourse(course);

        log.info("TEST INSERT AND SELECT");
        studentCourseMapper.insertStudent(student);
        assertNotNull(student.getId());
        studentSelect = studentCourseMapper.selectByIdStudent(student.getId());
        assertEquals(student, studentSelect);
        log.info("TEST INSERT AND SELECT SUCCESS");

        log.info("TEST UPDATE");
        student.setName("chekkk_test");
        student.setAge(34);
        studentCourseMapper.updateStudent(student);
        studentSelect = studentCourseMapper.selectByIdStudent(student.getId());
        assertEquals(student, studentSelect);
        log.info("TEST UPDATE SUCCESS");

        log.info("TEST DELETE");
        studentCourseMapper.deleteByIdStudent(student.getId());
        studentCourseMapper.deleteByNameCourse(course.getName());
        studentSelect = studentCourseMapper.selectByIdStudent(student.getId());
        assertNull(studentSelect);
        log.info("TEST DELETE SUCCESS");
    }

    @Test
    public void crudCourseTest() {
        Course course = new Course("go", "course go", 4);
        Course courseSelect;

        log.info("TEST INSERT AND SELECT");
        studentCourseMapper.insertCourse(course);
        courseSelect = studentCourseMapper.selectByNameCourse(course.getName());
        assertEquals(course, courseSelect);
        log.info("TEST INSERT AND SELECT SUCCESS");

        log.info("TEST UPDATE");
        course.setDescription("c++ best course");
        studentCourseMapper.updateCourse(course);
        courseSelect = studentCourseMapper.selectByNameCourse(course.getName());
        assertEquals(course, courseSelect);
        log.info("TEST UPDATE SUCCESS");

        log.info("TEST DELETE");
        studentCourseMapper.deleteByNameCourse(course.getName());
        courseSelect = studentCourseMapper.selectByNameCourse(course.getName());
        assertNull(courseSelect);
        log.info("TEST DELETE SUCCESS");
    }
}