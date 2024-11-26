package com.muhammadminhaz.inventorymanagementsystem.controller;

import com.muhammadminhaz.inventorymanagementsystem.service.DashboardService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class HomeController {


    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/dashboard"; // Redirect to dashboard if user is already logged in
        }
        return "home";
    }
}
