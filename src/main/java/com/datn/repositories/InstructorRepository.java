package com.datn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.entities.Instructor;

public interface InstructorRepository extends JpaRepository<Instructor, Integer> {
}
