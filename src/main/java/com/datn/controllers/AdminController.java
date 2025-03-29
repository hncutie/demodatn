package com.datn.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datn.entitys.Admin;
import com.datn.entitys.User;
import com.datn.repositories.AdminRepository;
import com.datn.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class AdminController {
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private UserRepository userRepository;

    // Hiển thị trang chỉnh sửa hồ sơ
    @GetMapping("/admin/profile")
    public String showAdminProfile(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "views/login"; // Chuyển hướng đến trang đăng nhập nếu chưa đăng nhập
        }

        Optional<Admin> adminOpt = adminRepository.findById(userId);
        Optional<User> userOpt = userRepository.findById(userId); // Lấy thông tin người dùng

        if (adminOpt.isPresent() && userOpt.isPresent()) {
            Admin admin = adminOpt.get();
            User user = userOpt.get();

            model.addAttribute("admin", admin); // Thêm admin vào mô hình
            model.addAttribute("user", user); // Thêm user vào mô hình
            return "views/admin/adminProfile"; // Trả về trang chỉnh sửa hồ sơ
        }

        model.addAttribute("error", "Không tìm thấy thông tin quản trị viên");
        return "views/admin/adminProfile"; // Quay lại trang chỉnh sửa hồ sơ với thông báo lỗi
    }

    // Xử lý cập nhật hồ sơ
    @PostMapping("/admin/profile")
    public String updateAdminProfile(@RequestParam("id") Integer id,
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("position") String position,
            RedirectAttributes redirectAttributes) {
        Optional<Admin> adminOpt = adminRepository.findById(id);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            admin.setName(name);
            admin.setPhone(phone);
            admin.setPosition(position);
            adminRepository.save(admin); // Lưu thay đổi vào cơ sở dữ liệu

            redirectAttributes.addFlashAttribute("success", "Cập nhật hồ sơ thành công!");
            return "redirect:/admin/profile"; // Chuyển hướng lại trang chỉnh sửa hồ sơ
        }

        redirectAttributes.addFlashAttribute("error", "Không tìm thấy thông tin quản trị viên");
        return "redirect:/admin/profile"; // Quay lại trang chỉnh sửa hồ sơ với thông báo lỗi
    }
}