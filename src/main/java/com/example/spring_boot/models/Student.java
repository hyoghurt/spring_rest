package com.example.spring_boot.models;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Long id;
    private String name;
    private Integer age;
    private Integer time_from;
    private Integer time_to;
}
