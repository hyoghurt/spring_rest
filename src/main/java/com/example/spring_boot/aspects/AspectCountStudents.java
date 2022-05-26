package com.example.spring_boot.aspects;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AspectCountStudents {
    private int count = 0;

    @After("@annotation(com.example.spring_boot.annotations.CountStudents)")
    public void countStudents() {
        ++count;
        System.out.println(count);
    }
}
