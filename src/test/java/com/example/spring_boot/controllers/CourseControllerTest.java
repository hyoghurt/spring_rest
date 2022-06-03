package com.example.spring_boot.controllers;

import com.example.spring_boot.models.Course;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WithMockUser(authorities = "ADMIN")
class CourseControllerTest {

    @Autowired
    public CourseControllerTest(WebApplicationContext context) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Course course = new Course("test_l", "test_l desc", 4);

    @Test
    @Order(1)
    public void createCourseSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/course")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isCreated()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.readValue(expectedString, Course.class), course);
    }

    @Test
    @Order(2)
    public void getCourseSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/course/test_l"))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.readValue(expectedString, Course.class), course);
    }

    @Test
    @Order(4)
    public void updateCourseSuccess() throws Exception {
        course.setDescription("update desc");

        MvcResult mvcResult = mockMvc.perform(put("/course/test_l")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        assertEquals(objectMapper.readValue(expectedString, Course.class), course);
    }

    @Test
    @Order(5)
    public void deleteCourseSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(delete("/course/test_l"))
                .andExpect(status().isOk()).andReturn();

        String expectedString = mvcResult.getResponse().getContentAsString();

        assertEquals(expectedString, "");
    }

    //NOT_FOUND_TEST__________________________________________

    @Test
    public void deleteCourseNotFound() throws Exception {
        mockMvc.perform(delete("/course/sfdsfdsf"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCourseNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(put("/course/sfsdfsdf")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(course)))
                .andExpect(status().isNotFound()).andReturn();

        Map<String, String> map = new HashMap<>();
        map.put("message", "Course not found");

        String expectedString = mvcResult.getResponse().getContentAsString();
        String actualString = objectMapper.writeValueAsString(map);

        assertEquals(expectedString, actualString);
    }

    @Test
    public void getCourseNotFound() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/course/sdfdsfsdf"))
                .andExpect(status().isNotFound()).andDo(print()).andReturn();

        Map<String, String> map = new HashMap<>();
        map.put("message", "Course not found");

        String expectedString = mvcResult.getResponse().getContentAsString();
        String actualString = objectMapper.writeValueAsString(map);

        assertEquals(expectedString, actualString);
    }
}