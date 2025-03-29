package com.datn.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserProfileDTO {
    private Integer id;
    private String email;
    private String name; // Họ tên từ bảng students, instructors, etc.
    private String phone;
    private String avatar;
}
