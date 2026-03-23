package com.example.elearning.service;

import com.example.elearning.model.Role;
import com.example.elearning.model.Student;
import com.example.elearning.repository.RoleRepository;
import com.example.elearning.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean existsByUsername(String username) {
        return studentRepository.existsByUsername(username);
    }

    public boolean existsByEmail(String email) {
        return studentRepository.existsByEmail(email);
    }

    /**
     * Đăng ký học viên mới - Password sẽ được mã hóa BCrypt trước khi lưu
     */
    public void registerStudent(String username, String email, String rawPassword) {
        // Kiểm tra tồn tại trước khi tạo
        if (existsByUsername(username)) {
            throw new RuntimeException("Username đã tồn tại!");
        }
        if (existsByEmail(email)) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        Student student = new Student();
        student.setUsername(username);
        student.setEmail(email);

        // ←←← BCrypt encode - Đây là dòng fix lỗi login
        student.setPassword(passwordEncoder.encode(rawPassword));

        // Gán role STUDENT mặc định
        Role studentRole = roleRepository.findByName("STUDENT")
                .orElseThrow(() -> new RuntimeException("Role STUDENT không tồn tại trong database!"));

        Set<Role> roles = new HashSet<>();
        roles.add(studentRole);
        student.setRoles(roles);

        studentRepository.save(student);
    }
}