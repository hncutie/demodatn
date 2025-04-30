package com.datn.controllers;

import com.datn.dtos.InstructorDTO;
import com.datn.entities.Instructor;
import com.datn.entities.Role;
import com.datn.entities.User;
import com.datn.repositories.InstructorRepository;
import com.datn.repositories.RoleRepository;
import com.datn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class InstructorController {

    private final UserRepository userRepository;
    private final InstructorRepository instructorRepository;
    private final RoleRepository roleRepository;

    // Hiển thị form đăng ký giảng viên
    @GetMapping("/register-instructor")
    public String showForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();

        if (user.getInstructor() != null) {
            return "redirect:/courses";
        }

        model.addAttribute("instructorDTO", new InstructorDTO());
        return "views/register-instructor";
    }

    // Xử lý form đăng ký giảng viên
    @PostMapping("/register-instructor")
    public String registerInstructor(
            @ModelAttribute("instructorDTO") InstructorDTO dto,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam("avatarFile") MultipartFile avatarFile) {

        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        Role instructorRole = roleRepository.findByName("ROLE_INSTRUCTOR")
                .orElseThrow(() -> new RuntimeException("Không tìm thấy role ROLE_INSTRUCTOR"));

        if (!user.getRoles().contains(instructorRole)) {
            user.getRoles().add(instructorRole);
        }

        String avatarUrl = null;
        if (!avatarFile.isEmpty()) {
            try {
                String fileName = UUID.randomUUID().toString() + "_" + avatarFile.getOriginalFilename();
                Path uploadPath = Paths.get(System.getProperty("user.dir"), "uploads");

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(fileName);
                avatarFile.transferTo(filePath.toFile());

                avatarUrl = "/uploads/" + fileName;

            } catch (IOException e) {
                throw new RuntimeException("Lỗi khi upload ảnh đại diện", e);
            }
        }

        Instructor instructor = Instructor.builder()
                .phone(dto.getPhone())
                .avatar(avatarUrl)
                .bio(dto.getBio())
                .experience(dto.getExperience())
                .user(user)
                .build();

        user.setInstructor(instructor);
        userRepository.save(user);

        return "views/instructor/instructorDashboard"; // nên dùng redirect
    }
}
