package com.datn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    private Integer id;

    @Column(nullable = false)
    private String phone;

    private String department;

    private Double salary;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}
