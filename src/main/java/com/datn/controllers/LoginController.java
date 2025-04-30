// package com.datn.controllers;

// import com.datn.entities.User;
// import com.datn.repositories.UserRepository;
// import jakarta.servlet.http.HttpSession;
// import lombok.RequiredArgsConstructor;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;

// import java.util.List;
// import java.util.stream.Collectors;

// @Controller
// @RequiredArgsConstructor
// public class LoginController {

//     private final UserRepository userRepository;
//     private final PasswordEncoder passwordEncoder;

//     @GetMapping("/login")
//     public String showLoginForm() {
//         return "views/login"; // Trả về trang login
//     }

//     @PostMapping("/login")
//     public String handleLogin(
//             @RequestParam("email") String email,
//             @RequestParam("password") String password,
//             Model model,
//             HttpSession session) {

//         User user = userRepository.findByEmail(email).orElse(null);
//         if (user == null) {
//             model.addAttribute("error", "Email không tồn tại!");
//             return "views/login";
//         }

//         if (!passwordEncoder.matches(password, user.getPassword())) {
//             model.addAttribute("error", "Mật khẩu không đúng!");
//             return "views/login";
//         }

//         // ✅ Thêm prefix ROLE_ cho authorities để Spring hiểu
//         List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
//                 .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())) // -> ROLE_ADMIN
//                 .collect(Collectors.toList());

//         UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
//                 user.getEmail(), null, authorities);
//         SecurityContextHolder.getContext().setAuthentication(authenticationToken);

//         // ✅ Lưu tên role gốc vào session để Thymeleaf dùng
//         if (!user.getRoles().isEmpty()) {
//             String roleName = user.getRoles().iterator().next().getName(); // vẫn là ADMIN
//             session.setAttribute("role", roleName); // cho Thymeleaf
//             System.out.println("Role lưu vào session: " + roleName);
//         } else {
//             session.removeAttribute("role");
//         }

//         return "redirect:/";
//     }
// }

package com.datn.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "views/login"; // trả về file login.html
    }

}
