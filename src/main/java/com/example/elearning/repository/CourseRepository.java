package com.example.elearning.repository;

import com.example.elearning.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    List<Course> findByNameContainingIgnoreCase(String name);
    List<Course> findByCategoryId(Integer categoryId);
    List<Course> findByNameContainingIgnoreCaseAndCategoryId(String name, Integer categoryId);
}
