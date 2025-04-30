package com.datn.controllers;

import com.datn.dtos.RegisterRequest;
import com.datn.entities.Role;
import com.datn.entities.User;
import com.datn.repositories.RoleRepository;
import com.datn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class RegisterController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "views/register"; // Tới file templates/views/register.html
    }

    @PostMapping("/register")
    public String handleRegister(@ModelAttribute("registerRequest") RegisterRequest request,
            BindingResult result,
            Model model) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            result.rejectValue("email", "error.registerRequest", "Email đã tồn tại!");
            return "views/register";
        }

        // Create new User
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Mã hóa mật khẩu

        // Set default role USER
        Optional<Role> userRole = roleRepository.findByName("USER");
        userRole.ifPresent(role -> user.setRoles(Collections.singleton(role)));

        userRepository.save(user);

        return "views/login"; // Sau khi đăng ký thành công, chuyển về trang login
    }
}
