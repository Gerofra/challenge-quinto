package com.universidadquinto.security;



import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.universidadquinto.entity.Role;
import com.universidadquinto.entity.User;
import com.universidadquinto.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> exist = userRepository.findByEmail(email);

        if (exist.isPresent()) {
        	User user = exist.get();
            return new org.springframework.security.core.userdetails.User(user.getEmail(),
                    user.getPassword(),
                    mapRolesToAuthorities(user.getRole()));
        }else{
            throw new UsernameNotFoundException("Email o contraseña incorreta.");
        }
    }


    
    private List<GrantedAuthority> mapRolesToAuthorities(Role role) {
    	List<GrantedAuthority> permissions = new ArrayList<>();
    	GrantedAuthority p = new SimpleGrantedAuthority(role.getName().name());
    	permissions.add(p);
        return permissions;
    }

}

