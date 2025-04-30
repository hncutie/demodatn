package com.datn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.datn.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

}
