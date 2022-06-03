package com.example.spring_boot.controllers;

import com.example.spring_boot.models.Course;
import com.example.spring_boot.models.StudentRequest;
import com.example.spring_boot.models.StudentResponse;
import com.example.spring_boot.models.StudentCourseEnrollment;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
@WithMockUser(authorities = "ADMIN")
class StudentControllerTest {

    @Autowired
    public StudentControllerTest(WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        notFoundJson = new HashMap<>();
        notFoundJson.put("message", "Student not found");
        objectMapper = new ObjectMapper();
    }

    private final MockMvc mockMvc;
    private final Map<String, String> notFoundJson;
    private final ObjectMapper objectMapper;

    private final Course course = new Course("java", "java course", 4);
    private final Course coursePython = new Course("python", "python course", 4);

    private final StudentResponse studentActual1 =
            new StudentResponse(1L, "Mick", 23, 14, 15, 3, course);
    private final StudentResponse studentActual2 =
            new StudentResponse(2L, "Lick", 13, 15, 16, 4, course);
    private final StudentResponse studentActual3 =
            new StudentResponse(3L, "Pick", 22, 12, 13, 3, coursePython);

    @Test
    public void createStudentSuccess() throws Exception {
        StudentRequest studentRequest =
                new StudentRequest(null, "TestStud", 12, 23, 24, "java", 4);
        StudentResponse studentResponse =
                new StudentResponse(studentRequest, course);

        MvcResult mvcResult = mockMvc.perform(post("/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isCreated()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();
        StudentResponse expectedStudent = objectMapper.readValue(expectedString, StudentResponse.class);
        studentResponse.setId(expectedStudent.getId());

        assertNotNull(expectedStudent.getId());
        assertEquals(objectMapper.readValue(expectedString, StudentResponse.class), studentResponse);
    }

    @Test
    public void getStudentSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/student/" + studentActual1.getId()))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();
        assertEquals(objectMapper.readValue(expectedString, StudentResponse.class), studentActual1);
    }

    @Test
    public void updateStudentSuccess() throws Exception {
        studentActual2.setAge(123);
        studentActual2.setTimeFrom(341);
        StudentRequest studentRequest = new StudentRequest(studentActual2);
        studentRequest.setId(null);

        MvcResult mvcResult = mockMvc.perform(put("/student/" + studentActual2.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.readValue(expectedString, StudentResponse.class), studentActual2);
    }

    @Test
    public void studentCourseEnrollment() throws Exception {
        StudentCourseEnrollment sce = new StudentCourseEnrollment();
        sce.setCourse("java");
        sce.setListStudent(Arrays.asList(2L, 4L));

        mockMvc.perform(post("/student/course_enrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sce)))
                .andExpect(status().isOk());

        MvcResult mvcResult;
        String expectedString;
        StudentResponse studentResponse;

        mvcResult = mockMvc.perform(get("/student/2"))
                .andExpect(status().isOk()).andReturn();
        expectedString = mvcResult.getResponse().getContentAsString();
        studentResponse = objectMapper.readValue(expectedString, StudentResponse.class);
        assertEquals(studentResponse.getCourse().getName(), "java");

        mvcResult = mockMvc.perform(get("/student/4"))
                .andExpect(status().isOk()).andReturn();
        expectedString = mvcResult.getResponse().getContentAsString();
        studentResponse = objectMapper.readValue(expectedString, StudentResponse.class);
        assertEquals(studentResponse.getCourse().getName(), "java");
    }

    @Test
    public void studentCourseEnrollmentException() throws Exception {
        StudentCourseEnrollment sce = new StudentCourseEnrollment();
        sce.setCourse("python");
        sce.setListStudent(Arrays.asList(2L, 1L));

        String expectedString;
        MvcResult mvcResult;
        StudentResponse studentResponse;

        mvcResult = mockMvc.perform(post("/student/course_enrollment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sce)))
                .andExpect(status().isNotFound()).andReturn();

        expectedString = mvcResult.getResponse().getContentAsString();
        notFoundJson.put("message", "StudentResponse id: 1 grade < course requiredGrade");
        String actualString = objectMapper.writeValueAsString(notFoundJson);
        assertEquals(expectedString, actualString);

        mvcResult = mockMvc.perform(get("/student/2"))
                .andExpect(status().isOk()).andReturn();
        expectedString = mvcResult.getResponse().getContentAsString();
        studentResponse = objectMapper.readValue(expectedString, StudentResponse.class);
        assertEquals(studentResponse.getCourse().getName(), "java");

        mvcResult = mockMvc.perform(get("/student/1"))
                .andExpect(status().isOk()).andReturn();
        expectedString = mvcResult.getResponse().getContentAsString();
        studentResponse = objectMapper.readValue(expectedString, StudentResponse.class);
        assertEquals(studentResponse.getCourse().getName(), "java");
    }

    @Test
    public void deleteStudentSuccess() throws Exception {
        mockMvc.perform(delete("/student/" + studentActual3.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/student/" + studentActual3.getId()))
                .andExpect(status().isNotFound());
    }


    //NOT_FOUND_TEST__________________________________________

    @Test
    public void updateStudentNotFound() throws Exception {
        StudentRequest studentRequest = new StudentRequest(studentActual3);
        studentRequest.setId(null);

        MvcResult mvcResult = mockMvc.perform(put("/student/234")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
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