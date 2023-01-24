package com.universidadquinto.dto;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.universidadquinto.entity.Course;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	
    private String id;
    @NotEmpty
    private String name;
    @NotEmpty
    private String lastname;
    @NotEmpty(message = "Email faltante")
    @Email
    private String email;
    @NotEmpty(message = "Contraseña faltante")
    private String password;  
    @DateTimeFormat(iso=ISO.DATE)
	private Date date;

    private int age;

    private String role;   
    
    private List<Course> courses;
    
}
