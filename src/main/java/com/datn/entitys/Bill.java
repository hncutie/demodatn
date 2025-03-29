package com.datn.entitys;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "bill")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    private Double price;

    @Column(name = "issued_at", columnDefinition = "DATETIME DEFAULT GETDATE()")
    private Date issuedAt;

    private String recipientEmail; // Địa chỉ email người nhận
    private String status; // Trạng thái hóa đơn (đã gửi, chưa gửi, đã thanh toán, chưa thanh toán)
}