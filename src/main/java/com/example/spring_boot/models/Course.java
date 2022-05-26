package com.example.spring_boot.models;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    private String name;
    private String description;
}
