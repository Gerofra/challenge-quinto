package com.universidadquinto.service;

import java.util.Date;
import java.util.List;

import com.universidadquinto.dto.UserDto;
import com.universidadquinto.entity.Role;
import com.universidadquinto.entity.User;

public interface UserService {
    void saveUser(UserDto userDto);

    void deleteUser(String id)  throws Exception;
    
    UserDto updateUser(User user, String id)  throws Exception;
    
    UserDto updateRole(String role, String id)  throws Exception;
    
    UserDto findByEmail(String email);
    
    UserDto findById(String id);
    
    User checkProfessorById(String id) throws Exception;

    UserDto setCourse(Long courseId, String userId) throws Exception;
    
    List<UserDto> findAllUsers();
    
    List<UserDto> findAllProfessors();
    
    Integer getUserAge(Date date);
    
    String getUserRole(Role role);
    
    UserDto convertEntityToDto(User user);
}
