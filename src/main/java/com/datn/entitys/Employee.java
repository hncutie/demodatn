package com.datn.entitys;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String department;
    private Double salary;

    @Column(name = "created_at", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date createdAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}