package com.datn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.entities.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}