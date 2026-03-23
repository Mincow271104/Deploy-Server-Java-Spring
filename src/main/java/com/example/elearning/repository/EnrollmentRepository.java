package com.example.elearning.repository;

import com.example.elearning.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Integer> {
    List<Enrollment> findByStudentStudentId(Integer studentId);
    boolean existsByStudentStudentIdAndCourseId(Integer studentId, Integer courseId);
    void deleteByStudentStudentIdAndCourseId(Integer studentId, Integer courseId);
}
