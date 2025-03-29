package com.datn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.entitys.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
}
