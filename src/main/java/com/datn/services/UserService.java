package com.datn.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.datn.dto.UserProfileDTO;
import com.datn.entitys.User;
import com.datn.repositories.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Autowired
    private EntityManager entityManager;

    public UserProfileDTO getUserProfile(Integer userId, String role) {
        String table = switch (role) {
            case "student" -> "students";
            case "instructor" -> "instructors";
            case "employee" -> "employees";
            case "moderator" -> "moderators";
            case "admin" -> "admins";
            default -> null;
        };

        if (table == null)
            return null;

        String sql = "SELECT u.id, u.email, t.name, t.phone, t.avatar " +
                "FROM users u JOIN " + table + " t ON u.id = t.id " +
                "WHERE u.id = :userId";

        Query query = entityManager.createNativeQuery(sql, Tuple.class);
        query.setParameter("userId", userId);
        Tuple result = (Tuple) query.getSingleResult();

        UserProfileDTO userProfile = new UserProfileDTO();
        userProfile.setId(result.get("id", Integer.class));
        userProfile.setEmail(result.get("email", String.class));
        userProfile.setName(result.get("name", String.class));
        userProfile.setPhone(result.get("phone", String.class));
        userProfile.setAvatar(result.get("avatar", String.class));

        return userProfile;
    }
}