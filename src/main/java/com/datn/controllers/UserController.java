package com.datn.controllers;

import com.datn.dto.UserProfileDTO;
import com.datn.entitys.Student;
import com.datn.entitys.User;
import com.datn.repositories.StudentRepository;
import com.datn.repositories.UserRepository;
import com.datn.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Hiển thị form đăng ký
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "views/register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
            @RequestParam("role") String role,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile) {

        // Thiết lập vai trò và mã hóa mật khẩu
        user.setRole(role);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Mã hóa mật khẩu

        // Lưu user vào cơ sở dữ liệu
        userRepository.save(user); // Lưu user trước để kích hoạt trigger

        // Nếu vai trò là student, bạn có thể xử lý thông tin avatar nếu cần
        if ("student".equals(role) && avatarFile != null && !avatarFile.isEmpty()) {
            try {
                String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                File uploadFolder = new File(uploadDir);
                if (!uploadFolder.exists())
                    uploadFolder.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
                File saveFile = new File(uploadDir + fileName);

                // Lưu tệp ảnh
                avatarFile.transferTo(saveFile);

                // Tạo đối tượng Student và lưu tên file vào bảng students
                Student student = new Student();
                student.setId(user.getId()); // Lấy ID từ user
                student.setName(name); // Đặt tên đúng từ request
                student.setPhone(phone);
                student.setAvatar(fileName); // Lưu tên file ảnh

                // Lưu đối tượng Student vào cơ sở dữ liệu
                studentRepository.save(student);

            } catch (IOException e) {
                e.printStackTrace();
                return "views/register"; // Quay lại form đăng ký nếu có lỗi
            }
        }

        return "views/login"; // Chuyển hướng đến trang đăng nhập
    }

    // Hiển thị form đăng nhập
    @GetMapping("/login")
    public String showLoginForm() {
        return "views/login";
    }

    // Xử lý đăng nhập
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
            @RequestParam("password") String password,
            HttpSession session, Model model) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(password, user.getPassword())) { // Kiểm tra mật khẩu
                session.setAttribute("userId", user.getId()); // Lưu userId vào session

                // Phân quyền và chuyển hướng
                switch (user.getRole()) {
                    case "admin":
                        return "views/admin/adminDashboard"; // Trang dành cho quản trị viên
                    case "student":
                        return "views/student/studentDashboard"; // Trang dành cho học viên
                    case "employee":
                        return "views/employee/employeeDashboard"; // Trang dành cho nhân viên
                    case "moderator":
                        return "views/moderator/moderatorDashboard"; // Trang dành cho kiểm duyệt viên
                    case "instructor":
                        return "views/instructor/instructorDashboard"; // Trang dành cho giảng viên
                    default:
                        model.addAttribute("error", "Vai trò không hợp lệ");
                        return "views/login"; // Quay lại trang đăng nhập
                }
            }
        }
        model.addAttribute("error", "Sai email hoặc mật khẩu");
        return "views/login";
    }

    // Đăng xuất
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "views/login";
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "views/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Student> studentOpt = studentRepository.findById(userId);

        if (userOpt.isPresent() && studentOpt.isPresent()) {
            User user = userOpt.get();
            Student student = studentOpt.get();

            // Thêm thông tin vào mô hình
            model.addAttribute("user", user);
            model.addAttribute("student", student);

            return "views/profile"; // Trả về trang thông tin cá nhân
        }

        return "views/login"; // Chuyển hướng nếu không tìm thấy thông tin
    }

    @PostMapping("/profile")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            return "redirect:/login";

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Student> studentOpt = studentRepository.findById(userId);

        if (userOpt.isPresent() && studentOpt.isPresent()) {
            User user = userOpt.get();
            Student student = studentOpt.get();

            // Cập nhật email nếu có
            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }

            student.setName(name);
            student.setPhone(phone);

            // Giữ nguyên ảnh nếu không chọn file mới
            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                    File uploadFolder = new File(uploadDir);
                    if (!uploadFolder.exists())
                        uploadFolder.mkdirs();

                    String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
                    File saveFile = new File(uploadDir + fileName);

                    avatarFile.transferTo(saveFile);

                    // Cập nhật ảnh mới vào database
                    student.setAvatar(fileName);
                    System.out.println("Ảnh đã lưu: " + saveFile.getAbsolutePath());

                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu ảnh!");
                    return "redirect:/profile";
                }
            }

            userRepository.save(user);
            studentRepository.save(student);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        }

        return "redirect:/profile";
    }

}
