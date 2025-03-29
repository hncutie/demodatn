package com.datn.entitys;

import java.util.Date;

import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "progress")
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    private String status = "not_started";

    private Date completedAt;
}