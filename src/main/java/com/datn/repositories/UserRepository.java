package com.datn.repositories;

import com.datn.entitys.User;

import jakarta.transaction.Transactional;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    // Cập nhật thông tin học viên
    @Transactional
    @Modifying
    @Query("UPDATE Student s SET s.name = ?1, s.phone = ?2 WHERE s.id = ?3")
    void updateStudent(String name, String phone, Integer userId);

    // Cập nhật thông tin giảng viên
    @Transactional
    @Modifying
    @Query("UPDATE Instructor i SET i.name = ?1, i.phone = ?2 WHERE i.id = ?3")
    void updateInstructor(String name, String phone, Integer userId);

    // Cập nhật thông tin nhân viên
    @Transactional
    @Modifying
    @Query("UPDATE Employee e SET e.name = ?1, e.phone = ?2 WHERE e.id = ?3")
    void updateEmployee(String name, String phone, Integer userId);

    // Cập nhật thông tin kiểm duyệt viên
    @Transactional
    @Modifying
    @Query("UPDATE Moderator m SET m.name = ?1, m.phone = ?2 WHERE m.id = ?3")
    void updateModerator(String name, String phone, Integer userId);

    // Cập nhật thông tin quản trị viên
    @Transactional
    @Modifying
    @Query("UPDATE Admin a SET a.name = ?1, a.phone = ?2 WHERE a.id = ?3")
    void updateAdmin(String name, String phone, Integer userId);
}
