package com.example.elearning.service;

import com.example.elearning.model.Course;
import com.example.elearning.model.Enrollment;
import com.example.elearning.model.Student;
import com.example.elearning.repository.EnrollmentRepository;
import com.example.elearning.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Enrollment> getEnrollmentsByStudent(Integer studentId) {
        return enrollmentRepository.findByStudentStudentId(studentId);
    }

    public boolean isAlreadyEnrolled(Integer studentId, Integer courseId) {
        return enrollmentRepository.existsByStudentStudentIdAndCourseId(studentId, courseId);
    }

    public void enroll(String username, Course course) {
        Student student = studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        Enrollment enrollment = new Enrollment(student, course);
        enrollmentRepository.save(enrollment);
    }

    public Set<Integer> getEnrolledCourseIds(Integer studentId) {
        return enrollmentRepository.findByStudentStudentId(studentId)
                .stream()
                .map(e -> e.getCourse().getId())
                .collect(Collectors.toSet());
    }

    public Student getStudentByUsername(String username) {
        return studentRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    @org.springframework.transaction.annotation.Transactional
    public void unenroll(Integer studentId, Integer courseId) {
        enrollmentRepository.deleteByStudentStudentIdAndCourseId(studentId, courseId);
    }
}
