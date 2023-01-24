package com.universidadquinto.dto;

import java.util.List;

import com.universidadquinto.entity.User;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
	
    private Long id;
    
    @NotEmpty
    private String name;

    private String professor;
    
    private String turno;
    
    private String start;
    
    private List<UserDto> students;
      
}
