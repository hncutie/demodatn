package com.datn.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "instructors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
public class Instructor {

    @Id
    private Integer id;

    @Column(nullable = false)
    private String phone;

    private String avatar;

    private String bio;

    private Integer experience;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
}
