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
    public void crudTest() {
        Student studentTest = new Student(null, "Lik", 12, 15, 18, new Course("course", "descrC"));
        Student studentFindId;

        studentMapper.create(studentTest);
        log.info(studentTest.toString());
        assertNotNull(studentTest.getId());
        studentFindId = studentMapper.findById(studentTest.getId());
        log.info(studentFindId.toString());
        assertEquals(studentTest, studentFindId);

        studentTest.getCourse().setName("chan");
        studentTest.setTimeTo(43);
        studentMapper.update(studentTest);
        log.info(studentTest.toString());
        studentFindId = studentMapper.findById(studentTest.getId());
        log.info(studentFindId.toString());
        assertEquals(studentTest, studentFindId);

        studentMapper.delete(studentTest.getId());
        log.info(studentTest.toString());
        studentFindId = studentMapper.findById(studentTest.getId());
        assertNull(studentFindId);
    }
}