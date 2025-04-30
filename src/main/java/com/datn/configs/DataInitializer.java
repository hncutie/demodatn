package com.datn.configs;

import com.datn.entities.Role;
import com.datn.entities.User;
import com.datn.repositories.RoleRepository;
import com.datn.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private void ensureRoleExists(String roleName) {
        roleRepository.findByName(roleName).orElseGet(() -> {
            Role role = Role.builder().name(roleName).build();
            return roleRepository.save(role);
        });
    }

    @PostConstruct
    @Transactional
    public void init() {
        String superAdminEmail = "ngoctthpd10040@gmail.com";

        // Tạo các vai trò cần thiết
        ensureRoleExists("ROLE_ADMIN");
        ensureRoleExists("ROLE_INSTRUCTOR");
        ensureRoleExists("ROLE_STUDENT");
        ensureRoleExists("ROLE_MODERATOR");
        ensureRoleExists("ROLE_EMPLOYEE");

        if (userRepository.existsByEmail(superAdminEmail)) {
            return; // Đã tồn tại Super Admin
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow();

        User superAdminUser = User.builder()
                .name("Super Admin")
                .email(superAdminEmail)
                .password(passwordEncoder.encode("superpassword"))
                .roles(Collections.singleton(adminRole))
                .build();

        userRepository.saveAndFlush(superAdminUser);

        System.out.println("✅ Super Admin đã được khởi tạo thành công!");
    }

}
