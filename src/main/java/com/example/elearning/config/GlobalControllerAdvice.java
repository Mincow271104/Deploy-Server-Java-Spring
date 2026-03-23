package com.example.elearning.config;

import com.example.elearning.model.Student;
import com.example.elearning.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private StudentRepository studentRepository;

    @ModelAttribute("currentUser")
    public Student currentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return studentRepository.findByUsername(authentication.getName()).orElse(null);
        }
        return null;
    }
}
