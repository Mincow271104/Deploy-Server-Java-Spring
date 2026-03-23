package com.example.elearning.controller;

import com.example.elearning.model.Enrollment;
import com.example.elearning.model.Student;
import com.example.elearning.repository.StudentRepository;
import com.example.elearning.service.EnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EnrollmentService enrollmentService;

    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/avatars/";

    @GetMapping("")
    public String accountPage(@RequestParam(value = "tab", defaultValue = "profile") String tab,
                              Model model, Authentication authentication) {
        Student student = studentRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));
        model.addAttribute("student", student);
        model.addAttribute("activeTab", tab);

        // Load enrollments for my-courses tab
        if ("my-courses".equals(tab)) {
            List<Enrollment> enrollments = enrollmentService.getEnrollmentsByStudent(student.getStudentId());
            int totalCredits = enrollments.stream()
                    .mapToInt(e -> e.getCourse().getCredits())
                    .sum();
            model.addAttribute("enrollments", enrollments);
            model.addAttribute("totalCredits", totalCredits);
        }
        return "account";
    }

    @PostMapping("/update")
    public String updateAccount(@RequestParam(required = false) String fullName,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) String phone,
                                @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {
        Student student = studentRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setFullName(fullName);
        student.setEmail(email);
        student.setPhone(phone);

        // Upload avatar
        if (avatarFile != null && !avatarFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                String originalFilename = avatarFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String newFilename = UUID.randomUUID().toString() + extension;
                Path filePath = uploadPath.resolve(newFilename);
                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                student.setAvatar("/uploads/avatars/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        studentRepository.save(student);
        redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        return "redirect:/account?tab=profile";
    }
}
