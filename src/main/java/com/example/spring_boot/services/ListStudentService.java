package com.example.spring_boot.services;

import com.example.spring_boot.annotations.CountStudents;
import com.example.spring_boot.models.Student;
import com.example.spring_boot.repositories.StudentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@EnableScheduling
public class ListStudentService {
    private StudentList studentList;

    @Autowired
    public ListStudentService(StudentList studentList) {
        this.studentList = studentList;
    }

    @Scheduled(cron = "*/5 * * * * *")
    @CountStudents
    public List<Student> getBusyStudents() {
        int hour = LocalTime.now().getHour();
        return studentList.getList().stream()
                .filter(s -> s.getTime_from() >= hour && s.getTime_to() <= hour)
                .collect(Collectors.toList());
    }
}
