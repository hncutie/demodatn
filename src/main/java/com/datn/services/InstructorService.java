package com.datn.services;

import com.datn.dtos.InstructorDTO;
import com.datn.entities.Instructor;
import com.datn.entities.Role;
import com.datn.entities.User;
import com.datn.repositories.InstructorRepository;
import com.datn.repositories.RoleRepository;
import com.datn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Transactional
    public void registerAsInstructor(User user, InstructorDTO dto) {
        // 1. Gán role INSTRUCTOR nếu chưa có
        Role instructorRole = roleRepository.findByName("ROLE_INSTRUCTOR")
                .orElseThrow(() -> new RuntimeException("Instructor role not found"));

        if (!user.getRoles().contains(instructorRole)) {
            user.getRoles().add(instructorRole);
            userRepository.save(user); // cập nhật User với role mới
        }

        // 2. Tạo Instructor mới
        Instructor instructor = Instructor.builder()
                .phone(dto.getPhone())
                .bio(dto.getBio())
                .avatar(dto.getAvatar())
                .experience(dto.getExperience())
                .user(user) // mapping đúng user
                .build();

        instructorRepository.save(instructor);
    }
}
