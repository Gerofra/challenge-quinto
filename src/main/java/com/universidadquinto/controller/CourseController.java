package com.universidadquinto.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.universidadquinto.dto.CourseDto;
import com.universidadquinto.dto.UserDto;
import com.universidadquinto.service.CourseService;
import com.universidadquinto.service.UserService;

import jakarta.validation.Valid;

@Controller
public class CourseController {


    private CourseService courseService;
    private UserService userService;

    public CourseController(CourseService courseService,
    		UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }
	
	
    @GetMapping("/courses")
    public String listRegisteredCourses(Model model){
        List<CourseDto> courses = courseService.findAllCourses();
        List<UserDto> professors = userService.findAllProfessors(); 
        model.addAttribute("professors", professors);
        if (courses != null) {
			model.addAttribute("allcourses", courses);						
		}
        
        return "courses";
    }
    
    @PostMapping("/courses/save")
    public String createCourse(@Valid @RequestParam("name") String name,
					    		@RequestParam("turno") String turno,
					    		@RequestParam("startTime") String startTime,
					    		@RequestParam(value="professorId", required=false) String professorId,
                                Model model){
    	
    	List<CourseDto> courses = courseService.findAllCourses();
    	List<UserDto> professors = userService.findAllProfessors(); 

        try {
			courseService.saveCourse(name, turno, startTime, professorId);
		} catch (Exception e) {
        	model.addAttribute("allcourses", courses);
        	model.addAttribute("professors", professors);
        	model.addAttribute("error", "No se puedo registrar el curso.");
            return "courses";
		}
        return "redirect:/courses?success";
    }

    @GetMapping("/courses/delete/{id}")  
    public String deleteCourse(@PathVariable Long id, Model model) {   	
  	
    List<CourseDto> courses = courseService.findAllCourses();
    List<UserDto> professors = userService.findAllProfessors(); 
    try {
		courseService.deleteCourse(id);
	} catch (Exception e) {
    	model.addAttribute("allcourses", courses);
    	model.addAttribute("professors", professors);
    	model.addAttribute("error", "No se puedo eliminar el curso.");
        return "courses";
	}  
    	return "redirect:/courses?success";
    }
    
    
    @GetMapping("/courses/update/{id}")
    public String update(@PathVariable("id") Long id,
    		RedirectAttributes redirectAttrs){
       	 	
    	CourseDto existing = courseService.findById(id); 	
        redirectAttrs.addFlashAttribute("editableCourse", existing);
              
        return "redirect:/courses";
    }
    
    
    @PostMapping("/courses/update/save")
    public String update(@Valid @RequestParam(value="name", required=false) String name,
    			@RequestParam(value="turno", required=false) String turno,
	    		@RequestParam(value="startTime", required=false) String startTime,
	    		@RequestParam(value="professorId", required=false) String professorId,
	    		@RequestParam("id") Long id,
	            Model model){

        try {
			courseService.updateCourse(name, turno, startTime, professorId, id);
		} catch (Exception e) {
			model.addAttribute("error", e);
			return "courses";
		}
        
        return "redirect:/courses?success";
    }
    
    
    @GetMapping("/courses/search")
    public String search(@RequestParam("id") Long id,
    		@RequestParam(value="student", required=false) String student,
    		RedirectAttributes redirectAttrs){
    	
    	List<UserDto> users = userService.findAllUsers();
    	redirectAttrs.addFlashAttribute("allusers", users);
    	 	
    	UserDto existing = userService.findById(student);
        redirectAttrs.addFlashAttribute("user", existing);
        
        
        return "redirect:/users";
    }
    
    @GetMapping("/courses/remove-professor/{id}")
    public String removeProfessor(@PathVariable("id") Long id,
    		RedirectAttributes redirectAttrs, Model model){
    	 	
    	CourseDto existing = courseService.findById(id);
    	try {
			courseService.removeProfessor(id);
			redirectAttrs.addFlashAttribute("editableCourse", existing);
		} catch (Exception e) {
			model.addAttribute("error", e);
			return "courses";
		}
                  
        return "redirect:/courses?success";
    }
    
}
