package com.universidadquinto.service.impl;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.universidadquinto.dto.UserDto;
import com.universidadquinto.entity.Course;
import com.universidadquinto.entity.Role;
import com.universidadquinto.entity.User;
import com.universidadquinto.enums.RoleEnum;
import com.universidadquinto.repository.CourseRepository;
import com.universidadquinto.repository.RoleRepository;
import com.universidadquinto.repository.UserRepository;
import com.universidadquinto.service.UserService;


@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private CourseRepository courseRepository;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           CourseRepository courseRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.courseRepository = courseRepository;
    }

    @Override
    public void saveUser(UserDto userDto) {
    	User user = new User();
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setDate(userDto.getDate());

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Role role = roleRepository.findByName(RoleEnum.ROLE_STUDENT);
        user.setRole(role);
        userRepository.save(user);
    }

    @Override
    public UserDto findByEmail(String email) {
    	Optional<User> exist = userRepository.findByEmail(email);
    	if (exist.isPresent()) {
			return convertEntityToDto(exist.get());
		}
		return null; 	  
    }
    
    @Override
    public UserDto findById(String id) {
    	Optional<User> exist = userRepository.findById(id);
    	if (exist.isPresent()) {
			return convertEntityToDto(exist.get());
		}
		return null; 
    }
    
    @Override
    public List<UserDto> findAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream().map((user) -> convertEntityToDto(user))
                .collect(Collectors.toList());
    }

    public UserDto convertEntityToDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setName(user.getName());
        userDto.setLastname(user.getLastname());
        userDto.setEmail(user.getEmail());
        userDto.setCourses(user.getCourses());
        userDto.setDate(user.getDate());
        userDto.setAge(getUserAge(user.getDate()));
        userDto.setRole(getUserRole(user.getRole()));
        return userDto;
    }

	@Override
	public UserDto updateUser(User user, String id) throws Exception {

			Optional<User> exist = userRepository.findById(id);
			
            if (exist.isPresent()) {
            	User updated = exist.get();
			
	            if (user.getName() != null && !user.getName().isEmpty()) {
	            	updated.setName(user.getName());
	            }
	            
	            if (user.getLastname() != null && !user.getLastname().isEmpty()) {
	            	updated.setLastname(user.getLastname());
	            }
	            
	            if (user.getEmail() != null && !user.getEmail().isEmpty()) {
	            	updated.setEmail(user.getEmail());
	            }
	            
	            if (user.getPassword() != null && !user.getPassword().isEmpty() && user.getPassword().length() > 7) {
	            	updated.setPassword(passwordEncoder.encode(user.getPassword()));
	            }
	            
	            if (user.getDate() != null) {
	            	updated.setDate(user.getDate());
	            }
	
	            userRepository.save(updated);
	            
				return convertEntityToDto(updated);
			} else {
				throw new Exception("No se puedo actualizar.");
			}
	}

	@Override
	public UserDto updateRole(String role, String id)  throws Exception {
		
		Optional<User> exist = userRepository.findById(id);
		Role newRole = roleRepository.findByName(RoleEnum.valueOf(role));
		
        if (exist.isPresent()) {
        	User updated = exist.get();
        	
        	if (role != null && !role.isEmpty()) {
	        	if (newRole != null) {
	        		updated.setRole(newRole);
	        	}
        	}
        	
        	userRepository.save(updated); 
        	return convertEntityToDto(updated);
        } else {
			throw new Exception("No se puedo actualizar el rol.");
		}	
        		
	}

	@Override
	public Integer getUserAge(Date date) {
		LocalDate d = LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
		LocalDate now = LocalDate.now();

		Period periodo = Period.between(d, now);
		return periodo.getYears();
	}

	@Override
	public String getUserRole(Role role) {

		RoleEnum r = roleRepository.findByName(role.getName()).getName();
		
		switch (r) {
			case ROLE_ADMIN: 
				return "Administrador";
		case ROLE_PROFESSOR: 
        	   return "Profesor";
		case ROLE_STUDENT:
        	   return "Estudiante";
		case ROLE_USER: 
        	   return "Usuario";
		default:
        	   return "Sin rol asignado";
       }
	}

	@Override
	public User checkProfessorById(String id) throws Exception {
			Optional<User> user = userRepository.findById(id);
	        
	        if (user.isPresent()) {			      
		        if (user.get().getRole() == roleRepository.findByName(RoleEnum.ROLE_PROFESSOR)) {
					return user.get();
				} else {
					throw new Exception("El usuario indicado no es un profesor registrado.");
				}
	        }
	        return null;
	}

	@Override
	public List<UserDto> findAllProfessors() {
		List<User> users = userRepository.findAllProfessors();
		return users.stream().map((user) -> convertEntityToDto(user))
	                .collect(Collectors.toList());
	}

	@Override
	public UserDto setCourse(Long courseId, String userId) throws Exception {
		
		Optional<User> exist = userRepository.findById(userId);
		
        if (exist.isPresent()) {
        	User updated = exist.get();
        	
        	List<Course> courses = new ArrayList<>();
        	boolean add = true;
        	
        	for (Course c: updated.getCourses()) {
				if (c.getId() != courseId){
					courses.add(c);
				} else {
					add = false;
				}
			}        	
        	if (add) {
        		courses.add(courseRepository.findById(courseId).get());
        	}
        
        	updated.setCourses(courses);
	        userRepository.save(updated);
	        
			return convertEntityToDto(updated);
		} else {
			throw new Exception("No se puedo actualizar.");
		}
        
	}

	@Override
	public void deleteUser(String id) throws Exception {
		try {
			userRepository.deleteById(id);	
		} catch (Exception e) {
			throw new Exception("No se pudo eliminar.");
		}
		
	}
	
	
	
	
	
	
	

}
