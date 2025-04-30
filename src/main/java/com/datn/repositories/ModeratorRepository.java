package com.datn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.entities.Moderator;

public interface ModeratorRepository extends JpaRepository<Moderator, Integer> {

}
