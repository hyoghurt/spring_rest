package com.example.spring_boot.util;

import com.example.spring_boot.models.Course;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class CourseCache {
    private final ConcurrentMap<String, Course> cash;

    public CourseCache() {
        this.cash = new ConcurrentHashMap<>();
    }

    public Course get(String name) {
        return cash.get(name);
    }

    public void add(Course course) {
        cash.putIfAbsent(course.getName(), course);
    }

    public void replace(String name, Course course) {
        cash.replace(name, course);
    }

    public void remove(String name) {
        cash.remove(name, cash.get(name));
    }

    public long size() {
        return cash.size();
    }
}
