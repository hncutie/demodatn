package com.datn.configs;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.datn.entities.User;
import com.datn.repositories.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        private final UserRepository userRepository;

        public CustomAuthenticationSuccessHandler(UserRepository userRepository) {
                this.userRepository = userRepository;
        }

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request,
                        HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {
                HttpSession session = request.getSession();
                String email = authentication.getName(); // Lấy email người dùng đăng nhập
                User user = userRepository.findByEmail(email).orElse(null);

                if (user != null) {
                        // Lấy tất cả các role của người dùng
                        List<String> roleNames = user.getRoles().stream()
                                        .map(role -> role.getName())
                                        .collect(Collectors.toList());

                        // Xác định quyền hạn của các roles
                        String highestRole = getHighestRole(roleNames); // Lấy role có quyền hạn cao nhất

                        session.setAttribute("roleName", highestRole); // Lưu role có quyền hạn cao nhất vào session
                        session.setAttribute("userId", user.getId()); // Lưu userId vào session
                        session.setAttribute("email", user.getEmail()); // Lưu email vào session
                }

                response.sendRedirect("/"); // Chuyển hướng người dùng về trang chủ
        }

        // Phương thức xác định role có quyền hạn cao nhất
        private String getHighestRole(List<String> roles) {
                // Đây là thứ tự ưu tiên role. ADMIN có quyền cao nhất, rồi đến MODERATOR,
                // EMPLOYEE, STUDENT, v.v.
                List<String> rolePriority = List.of("ADMIN", "MODERATOR", "EMPLOYEE", "INSTRUCTOR", "STUDENT");

                return roles.stream()
                                .filter(rolePriority::contains) // Chỉ chọn những role có trong danh sách quyền hạn
                                .max((role1, role2) -> Integer.compare(rolePriority.indexOf(role1),
                                                rolePriority.indexOf(role2)))
                                .orElse("STUDENT"); // Nếu không có role nào thì mặc định là STUDENT
        }
}
