package com.example.spring_boot.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @NotBlank(message = "name is empty")
    private String name;

    @NotBlank(message = "password is empty")
    private String password;

    @Null
    private String roles;
}
