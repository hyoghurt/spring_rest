package com.example.spring_boot.mappers;

import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.Student;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class StudentMapperTest {

    @Autowired
    StudentMapper studentMapper;

    @Test
    public void selectMaxAgeAvgCourseTest() {
        Course course = studentMapper.selectCourseMaxAgeAvg();
        if (course != null)
            System.out.println(course.toString());
    }

    @Test
    public void crudStudentTest() {
        Course course = new Course("c++", "best course c++");
        Course courseSelect;
        Student student = new Student(null, "Lik", 12, 15, 18, course);
        Student studentSelect;

        studentMapper.insertCourse(course);

        log.info("TEST INSERT AND SELECT");
        studentMapper.insertStudent(student);
        assertNotNull(student.getId());
        studentSelect = studentMapper.selectByIdStudent(student.getId());
        assertEquals(student, studentSelect);
        log.info("TEST INSERT AND SELECT SUCCESS");

        log.info("TEST UPDATE");
        student.setName("chekkk_test");
        student.setAge(34);
        studentMapper.updateStudent(student);
        studentSelect = studentMapper.selectByIdStudent(student.getId());
        assertEquals(student, studentSelect);
        log.info("TEST UPDATE SUCCESS");

        log.info("TEST DELETE");
        studentMapper.deleteByIdStudent(student.getId());
        studentMapper.deleteByNameCourse(course.getName());
        studentSelect = studentMapper.selectByIdStudent(student.getId());
        assertNull(studentSelect);
        log.info("TEST DELETE SUCCESS");
    }

    @Test
    public void crudCourseTest() {
        Course course = new Course("go", "course go");
        Course courseSelect;

        log.info("TEST INSERT AND SELECT");
        studentMapper.insertCourse(course);
        courseSelect = studentMapper.selectByNameCourse(course.getName());
        assertEquals(course, courseSelect);
        log.info("TEST INSERT AND SELECT SUCCESS");

        log.info("TEST UPDATE");
        course.setDescription("c++ best course");
        studentMapper.updateCourse(course);
        courseSelect = studentMapper.selectByNameCourse(course.getName());
        assertEquals(course, courseSelect);
        log.info("TEST UPDATE SUCCESS");

        log.info("TEST DELETE");
        studentMapper.deleteByNameCourse(course.getName());
        courseSelect = studentMapper.selectByNameCourse(course.getName());
        assertNull(courseSelect);
        log.info("TEST DELETE SUCCESS");
    }
}