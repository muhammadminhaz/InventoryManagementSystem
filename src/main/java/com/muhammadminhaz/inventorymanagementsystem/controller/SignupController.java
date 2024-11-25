package com.muhammadminhaz.inventorymanagementsystem.controller;

import com.muhammadminhaz.inventorymanagementsystem.dao.AdminDAO;
import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import com.muhammadminhaz.inventorymanagementsystem.model.Product;
import com.muhammadminhaz.inventorymanagementsystem.service.AdminService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

    private final AdminDAO adminDAO;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;

    public SignupController(AdminDAO adminDAO, PasswordEncoder passwordEncoder, AdminService adminService) {
        this.adminDAO = adminDAO;
        this.passwordEncoder = passwordEncoder;
        this.adminService = adminService;
    }


    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "signup"; // Thymeleaf template for sign-up
    }

    @PostMapping("/signup")
    public String processSignup(@ModelAttribute Admin admin) {
        try {
            adminService.signUp(admin);
            return "redirect:/profile";
        } catch (IllegalArgumentException e) {
            return "redirect:/signup?error=" + e.getMessage();
        }
    }
}
