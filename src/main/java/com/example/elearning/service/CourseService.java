package com.example.elearning.service;

import com.example.elearning.model.Course;
import com.example.elearning.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> searchCourses(String keyword, Integer categoryId) {
        boolean hasKeyword = keyword != null && !keyword.trim().isEmpty();
        boolean hasCategory = categoryId != null;

        if (hasKeyword && hasCategory) {
            return courseRepository.findByNameContainingIgnoreCaseAndCategoryId(keyword.trim(), categoryId);
        } else if (hasKeyword) {
            return courseRepository.findByNameContainingIgnoreCase(keyword.trim());
        } else if (hasCategory) {
            return courseRepository.findByCategoryId(categoryId);
        }
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(Integer id) {
        return courseRepository.findById(id);
    }

    public Course saveCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Integer id) {
        courseRepository.deleteById(id);
    }
}
