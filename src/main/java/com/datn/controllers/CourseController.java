package com.datn.controllers;

import com.datn.repositories.CategoryRepository;
import com.datn.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class CourseController {

    private final CategoryRepository categoryRepository;
    private final CourseRepository courseRepository;

    @GetMapping("/courses")
    public String showCourses(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("courses", courseRepository.findAll());
        return "views/home"; // tên file HTML (courses.html) trong src/main/resources/templates
    }
}
