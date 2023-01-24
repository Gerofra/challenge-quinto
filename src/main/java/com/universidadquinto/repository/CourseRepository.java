package com.universidadquinto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.universidadquinto.entity.Course;


public interface CourseRepository extends JpaRepository<Course, Long> {
	
	Course findByName(String name);

	@Query("SELECT c FROM Course c JOIN FETCH c.students s WHERE s.id != :id")
	List<Course> findNoStudentCourses(@Param("id") String id);

	
}
