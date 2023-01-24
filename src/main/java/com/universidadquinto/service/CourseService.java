package com.universidadquinto.service;

import java.util.List;

import com.universidadquinto.dto.CourseDto;

public interface CourseService {
	
    void saveCourse(String name, String turno, String startTime, String professorId) throws Exception;
    
    void deleteCourse(Long id)  throws Exception;

    CourseDto updateCourse(String name, String turno, String startTime, String professorId, Long id) throws Exception;
    
    CourseDto findByName(String name);
    
    CourseDto findById(Long id);
    
    CourseDto setStudent(Long courseId, String userId) throws Exception;

    List<CourseDto> findAllCourses();
    
    List<CourseDto> findStudentCourses(String id);
    
    List<CourseDto> findNoStudentCourses(String id);
}
