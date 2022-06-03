package com.example.spring_boot.controllers;

import com.example.spring_boot.models.StudentRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class StudentBasicAuthenticationTest {

    @Autowired
    public StudentBasicAuthenticationTest(WebApplicationContext context) {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    private final MockMvc mvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final StudentRequest studentRequest = new StudentRequest(null,"TestStud", 12, 23, 24, "java", 4);
    private final String url = "/studentResponse/61";

    @Test
    public void getUnauthorized() throws Exception {
        mvc.perform(get(url)).andExpect(status().isUnauthorized());
    }

    @Test
    public void deleteUnauthorized() throws Exception {
        mvc.perform(delete(url)).andExpect(status().isUnauthorized());
    }

    @Test
    public void putUnauthorized() throws Exception {
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void postUnauthorized() throws Exception {
        mvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void getUserAuthorizer() throws Exception {
        mvc.perform(get(url)).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void deleteUserAuthorizer() throws Exception {
        mvc.perform(delete(url)).andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void putUserAuthorizer() throws Exception {
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "USER")
    public void postUserAuthorizer() throws Exception {
        mvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void getAdminAuthorizer() throws Exception {
        mvc.perform(get(url)).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void deleteAdminAuthorizer() throws Exception {
        mvc.perform(delete(url)).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void putAdminAuthorizer() throws Exception {
        mvc.perform(put(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void postAdminAuthorizer() throws Exception {
        mvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentRequest)))
                .andExpect(status().isCreated());
    }
}
