package com.example.elearning.controller;

import com.example.elearning.model.Course;
import com.example.elearning.model.Enrollment;
import com.example.elearning.model.Student;
import com.example.elearning.service.CourseService;
import com.example.elearning.service.EnrollmentService;
import com.example.elearning.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/enroll")
public class EnrollmentController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("")
    public String enrollPage(@RequestParam(required = false) String keyword,
                             @RequestParam(required = false) Integer categoryId,
                             Model model, Authentication authentication) {
        String username = authentication.getName();
        Student student = enrollmentService.getStudentByUsername(username);

        model.addAttribute("courses", courseService.searchCourses(keyword, categoryId));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        Set<Integer> enrolledIds = enrollmentService.getEnrolledCourseIds(student.getStudentId());
        model.addAttribute("enrolledIds", enrolledIds);
        return "enroll";
    }

    @PostMapping("/{courseId}")
    public String enroll(@PathVariable Integer courseId,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Student student = enrollmentService.getStudentByUsername(username);

        // Kiểm tra đã đăng ký chưa
        if (enrollmentService.isAlreadyEnrolled(student.getStudentId(), courseId)) {
            redirectAttributes.addFlashAttribute("error", "Bạn đã đăng ký học phần này rồi!");
            return "redirect:/enroll";
        }

        // Kiểm tra course tồn tại
        Optional<Course> course = courseService.getCourseById(courseId);
        if (course.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy học phần!");
            return "redirect:/enroll";
        }

        enrollmentService.enroll(username, course.get());
        redirectAttributes.addFlashAttribute("success", "Đăng ký học phần thành công!");
        return "redirect:/enroll";
    }

    // ==================== MY COURSES ====================
    @GetMapping("/my-courses")
    public String myCourses(Model model, Authentication authentication) {
        String username = authentication.getName();
        Student student = enrollmentService.getStudentByUsername(username);
        List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student.getStudentId());
        
        int totalCredits = enrollments.stream()
                .mapToInt(e -> e.getCourse().getCredits())
                .sum();
        
        model.addAttribute("enrollments", enrollments);
        model.addAttribute("totalCredits", totalCredits);
        return "my_courses";
    }

    // ==================== CANCEL ENROLLMENT ====================
    @PostMapping("/cancel/{courseId}")
    public String cancelEnrollment(@PathVariable Integer courseId,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        Student student = enrollmentService.getStudentByUsername(username);
        enrollmentService.unenroll(student.getStudentId(), courseId);
        redirectAttributes.addFlashAttribute("success", "Đã hủy đăng ký học phần thành công!");
        return "redirect:/account?tab=my-courses";
    }
}
