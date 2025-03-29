package com.datn.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.datn.entitys.Instructor;
import com.datn.entitys.User;
import com.datn.repositories.InstructorRepository;
import com.datn.repositories.UserRepository;

import jakarta.servlet.http.HttpSession;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Controller
public class InstructorProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @GetMapping("/instructor/profile")
    public String showProfile(HttpSession session, Model model) {
        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Instructor> instructorOpt = instructorRepository.findById(userId);

        if (userOpt.isPresent() && instructorOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            model.addAttribute("instructor", instructorOpt.get());
            return "views/instructor/instructorProfile";
        }

        return "redirect:/login";
    }

    @PostMapping("/instructor/profile")
    public String updateProfile(
            @RequestParam("name") String name,
            @RequestParam("phone") String phone,
            @RequestParam("bio") String bio,
            @RequestParam("experience") int experience,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        Integer userId = (Integer) session.getAttribute("userId");
        if (userId == null)
            return "redirect:/login";

        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Instructor> instructorOpt = instructorRepository.findById(userId);

        if (userOpt.isPresent() && instructorOpt.isPresent()) {
            User user = userOpt.get();
            Instructor instructor = instructorOpt.get();

            if (email != null && !email.isEmpty()) {
                user.setEmail(email);
            }

            instructor.setName(name);
            instructor.setPhone(phone);
            instructor.setBio(bio);
            instructor.setExperience(experience);

            if (avatarFile != null && !avatarFile.isEmpty()) {
                try {
                    String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
                    File uploadFolder = new File(uploadDir);
                    if (!uploadFolder.exists())
                        uploadFolder.mkdirs();

                    String fileName = System.currentTimeMillis() + "_" + avatarFile.getOriginalFilename();
                    File saveFile = new File(uploadDir + fileName);

                    avatarFile.transferTo(saveFile);
                    instructor.setAvatar(fileName);

                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("error", "Lỗi khi lưu ảnh!");
                    return "redirect:/instructor/instructorProfile";
                }
            }

            userRepository.save(user);
            instructorRepository.save(instructor);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
        }

        return "redirect:/instructor/instructorProfile";
    }
}
