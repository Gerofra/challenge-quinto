package com.universidadquinto.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.universidadquinto.entity.User;


public interface UserRepository extends JpaRepository<User, String> {
	
	Optional<User> findByEmail(String email);
    
    Optional<User> findById(String id);
    
	@Query("SELECT u FROM User u WHERE u.role.id = '3'")
	List<User> findAllProfessors();

    
}
