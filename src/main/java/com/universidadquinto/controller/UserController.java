package com.universidadquinto.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.universidadquinto.dto.CourseDto;
import com.universidadquinto.dto.UserDto;
import com.universidadquinto.entity.User;
import com.universidadquinto.service.CourseService;
import com.universidadquinto.service.UserService;

import jakarta.validation.Valid;

@Controller
public class UserController {

	
    private UserService userService;
    private CourseService courseService;

    public UserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }
    

    
    @GetMapping("/users")
    public String listRegisteredUsers(Model model){
        List<UserDto> users = userService.findAllUsers();
        model.addAttribute("allusers", users);
        return "users";
    }
    
    @GetMapping("/users/delete/{id}")  
    public String deleteUser(@PathVariable String id, Model model) {   	
  	
        List<UserDto> users = userService.findAllUsers();
    try {
		userService.deleteUser(id);
	} catch (Exception e) {
		model.addAttribute("allusers", users);
    	model.addAttribute("error", "No se puedo eliminar el usuario.");
        return "users";
	}  
    	return "redirect:/users?success";
    }
    
    @GetMapping("/users/update/{email}")
    public String update(@PathVariable("email") String email,
    		RedirectAttributes redirectAttrs){
    	
    	List<UserDto> users = userService.findAllUsers();
    	redirectAttrs.addFlashAttribute("allusers", users);
    	 	
    	UserDto existing = userService.findByEmail(email);
        redirectAttrs.addFlashAttribute("user", existing);
        
        
        return "redirect:/users";
    }
    
    
    
    @PostMapping("/users/update/save/{id}")
    public String update(@Valid @ModelAttribute("user") User user,
    							@RequestParam("id") String id,
    							BindingResult result,
    							Model model){
        UserDto existing = userService.findById(id);
 
        if (result.hasErrors()) {
            model.addAttribute("user", existing);
            return "users";
        }
      
        try {
			userService.updateUser(user, id);
		} catch (Exception e) {
			model.addAttribute("error", e);
			return "users";
		}
        
        return "redirect:/users?success";
    }
    
    @PostMapping("/users/role/save")
    public String update(@RequestParam("role") String role,
    							@RequestParam("id") String id,
                                Model model){
    	
        try {
			userService.updateRole(role, id);
		} catch (Exception e) {
			model.addAttribute("error", e);
			return "users";
		}
        
        return "redirect:/users?success";
    }
    
    
    @GetMapping("/home")
    public String home(Authentication auth, Model model){
    	
    	UserDto u = userService.findByEmail(auth.getName());
    	model.addAttribute("auth", auth);
    	model.addAttribute("currentUser", u);
    	
    	GrantedAuthority first = auth.getAuthorities().iterator().next();
    	if (first.getAuthority().toString().equalsIgnoreCase("ROLE_ADMIN")) {
    		model.addAttribute("priv", true);
    	} else {
    		model.addAttribute("allcourses", courseService.findStudentCourses(u.getId()));
    		model.addAttribute("nocourses", courseService.findAllCourses());
    	}
    	
        return "home";
    }
    
    @GetMapping("/home/join/{courseId}/{userId}")
    public String update(@Valid @ModelAttribute("courseId") Long courseId,
    							@ModelAttribute("userId") String id,
    							RedirectAttributes redirectAttrs){
      
        try {
			courseService.setStudent(courseId, id);
			userService.setCourse(courseId, id);
			redirectAttrs.addFlashAttribute("success", "Se realizó en cambio correctamente");
		} catch (Exception e) {
			redirectAttrs.addFlashAttribute("error", e);
			return "home";
		}
        
        return "redirect:/home";
    }
    
}
