package com.example.spring_boot.repositories;

import com.example.spring_boot.models.Student;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Getter
@Component
@PropertySource("classpath:application.properties")
public class StudentList {
    private List<Student> list;

    public StudentList(@Value("${path.students}")String csvFileName) {
        parseFile(csvFileName);
    }

    private void parseFile(String csvFileName) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream(csvFileName);
        assert resourceAsStream != null;
        InputStreamReader inputStreamReader = new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8);

        AtomicLong id = new AtomicLong(1);

        this.list = new BufferedReader(inputStreamReader).lines()
                .skip(1)
                .map(s -> {
                    String[] arr = s.split(",");
                    Student student = new Student();
                    student.setId(id.getAndIncrement());
                    student.setName(arr[0].replaceAll("\"", ""));
                    student.setAge(Integer.parseInt(arr[1]));
                    student.setTime_from(Integer.parseInt(arr[2]));
                    student.setTime_to(Integer.parseInt(arr[3]));
                    return student;
                }).collect(Collectors.toList());
    }
}
