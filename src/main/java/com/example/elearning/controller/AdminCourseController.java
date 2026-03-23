package com.example.elearning.controller;

import com.example.elearning.model.Course;
import com.example.elearning.repository.CategoryRepository;
import com.example.elearning.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/courses")
public class AdminCourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryRepository categoryRepository;

    // Thư mục lưu ảnh upload
    private static final String UPLOAD_DIR = "src/main/resources/static/uploads/";

    // ==================== LIST (Admin Page) ====================
    @GetMapping("")
    public String listCourses(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) Integer categoryId,
                              Model model) {
        model.addAttribute("courses", courseService.searchCourses(keyword, categoryId));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategoryId", categoryId);
        return "admin_courses";
    }

    // ==================== CREATE ====================
    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("pageTitle", "Thêm học phần mới");
        return "course_form";
    }

    @PostMapping("/create")
    public String createCourse(@ModelAttribute Course course,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {
        handleImageUpload(course, imageFile);
        courseService.saveCourse(course);
        redirectAttributes.addFlashAttribute("success", "Thêm học phần thành công!");
        return "redirect:/admin/courses";
    }

    // ==================== UPDATE ====================
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Course> course = courseService.getCourseById(id);
        if (course.isPresent()) {
            model.addAttribute("course", course.get());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("pageTitle", "Chỉnh sửa học phần");
            return "course_form";
        }
        redirectAttributes.addFlashAttribute("error", "Không tìm thấy học phần!");
        return "redirect:/admin/courses";
    }

    @PostMapping("/edit/{id}")
    public String updateCourse(@PathVariable Integer id,
                               @ModelAttribute Course course,
                               @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                               RedirectAttributes redirectAttributes) {
        course.setId(id);
        handleImageUpload(course, imageFile);
        courseService.saveCourse(course);
        redirectAttributes.addFlashAttribute("success", "Cập nhật học phần thành công!");
        return "redirect:/admin/courses";
    }

    // ==================== DELETE ====================
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("success", "Xóa học phần thành công!");
        return "redirect:/admin/courses";
    }

    // ==================== ADD CATEGORY ====================
    @PostMapping("/add-category")
    public String addCategory(@RequestParam String categoryName, RedirectAttributes redirectAttributes) {
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            com.example.elearning.model.Category category = new com.example.elearning.model.Category(categoryName.trim());
            categoryRepository.save(category);
            redirectAttributes.addFlashAttribute("success", "Thêm danh mục thành công!");
        }
        return "redirect:/admin/courses";
    }

    // ==================== HELPER: Upload ảnh ====================
    private void handleImageUpload(Course course, MultipartFile imageFile) {
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                // Tạo thư mục nếu chưa có
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Tạo tên file unique
                String originalFilename = imageFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String newFilename = UUID.randomUUID().toString() + extension;

                // Lưu file
                Path filePath = uploadPath.resolve(newFilename);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Set đường dẫn ảnh cho course
                course.setImage("/uploads/" + newFilename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Nếu không upload file mới, giữ nguyên URL (nếu có nhập)
    }
}
