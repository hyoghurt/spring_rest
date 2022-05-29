package com.example.spring_boot.controllers;

import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.Student;
import com.example.spring_boot.models.StudentCourseEnrollment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class StudentControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final MockMvc mockMvc;
    private final Course course = new Course("java", "java course", 4);
    private final Student student = new Student(5L, "TestStud", 12, 23, 24, course, 3);
    private final String url = "/student/5";
    private final Map<String, String> notFoundJson;

    @Autowired
    public StudentControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        notFoundJson = new HashMap<>();
        notFoundJson.put("message", "Student not found");
    }

    @Test
    @Order(1)
    public void createStudentSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isCreated()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.readValue(expectedString, Student.class), student);
    }

    @Test
    @Order(2)
    public void getStudentSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.readValue(expectedString, Student.class), student);
    }

    @Test
    @Order(3)
    public void getAllStudentSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/student"))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        List<Student> students = objectMapper.readValue(expectedString, new TypeReference<>() {});

        assertTrue(students.size() >= 3);
        assertTrue(students.stream().anyMatch(s -> s.equals(student)));
    }

    @Test
    @Order(4)
    public void updateStudentSuccess() throws Exception {
        student.setAge(123);

        MvcResult mvcResult = mockMvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.readValue(expectedString, Student.class), student);
    }

    @Test
    @Order(5)
    public void studentCourseEnrollment() throws Exception {
        StudentCourseEnrollment sce = new StudentCourseEnrollment();
        sce.setCourse("java");
        sce.setListStudent(Arrays.asList(2L, 4L));

        mockMvc.perform(post("/student/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sce)))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(get("/student"))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();
        List<Student> students = objectMapper.readValue(expectedString, new TypeReference<>() {});

        Student student1 = students.get(1);
        Student student2 = students.get(3);

        assertEquals(student1.getId(), 2L);
        assertEquals(student2.getId(), 4L);
        assertEquals(student1.getCourse().getName(), "java");
        assertEquals(student2.getCourse().getName(), "java");
    }

    @Test
    @Order(6)
    public void studentCourseEnrollmentException() throws Exception {
        StudentCourseEnrollment sce = new StudentCourseEnrollment();
        sce.setCourse("python");
        sce.setListStudent(Arrays.asList(2L, 1L));

        MvcResult mvcResult1 = mockMvc.perform(post("/student/course")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sce)))
                .andExpect(status().isNotFound()).andReturn();


        String expectedString = mvcResult1.getResponse().getContentAsString();
        notFoundJson.put("message", "Student id: 1 grade < course requiredGrade");
        String actualString = objectMapper.writeValueAsString(notFoundJson);

        assertEquals(expectedString, actualString);

        MvcResult mvcResult = mockMvc.perform(get("/student"))
                .andExpect(status().isOk()).andReturn();

        expectedString = mvcResult.getResponse().getContentAsString();
        List<Student> students = objectMapper.readValue(expectedString, new TypeReference<>() {});

        Student student1 = students.get(0);
        Student student2 = students.get(1);

        assertEquals(student1.getId(), 1L);
        assertEquals(student2.getId(), 2L);
        assertEquals(student1.getCourse().getName(), "java");
        assertEquals(student2.getCourse().getName(), "java");
    }

    @Test
    @Order(7)
    public void deleteStudentSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete(url))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        assertEquals(expectedString, "");
    }


    //NOT_FOUND_TEST__________________________________________

    @Test
    public void deleteStudentNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/student/432"))
                .andExpect(status().isNotFound()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();
        String actualString = objectMapper.writeValueAsString(notFoundJson);

        assertEquals(expectedString, actualString);
    }

    @Test
    public void updateStudentNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/student/234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)))
                .andExpect(status().isNotFound()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();
        String actualString = objectMapper.writeValueAsString(notFoundJson);

        assertEquals(expectedString, actualString);
    }

    @Test
    public void getStudentNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/student/123"))
                .andExpect(status().isNotFound()).andDo(print()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();
        String actualString = objectMapper.writeValueAsString(notFoundJson);

        assertEquals(expectedString, actualString);
    }
}