package com.datn.entitys;

import java.util.Date;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "students")
public class Student {
    @Id
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String avatar;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", updatable = false)
    private Date createdAt = new Date(); // Lấy ngày hiện tại khi tạo Student

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}
