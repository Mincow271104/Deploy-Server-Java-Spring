package com.example.elearning.repository;

import com.example.elearning.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
