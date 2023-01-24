package com.universidadquinto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.universidadquinto.entity.Role;
import com.universidadquinto.enums.RoleEnum;


public interface RoleRepository extends JpaRepository<Role, Long> {
	
    Role findByName(RoleEnum name);
    
}
