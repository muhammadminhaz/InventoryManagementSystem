package com.muhammadminhaz.inventorymanagementsystem.controller;

import com.muhammadminhaz.inventorymanagementsystem.dao.AdminDAO;
import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import com.muhammadminhaz.inventorymanagementsystem.model.Invoice;
import com.muhammadminhaz.inventorymanagementsystem.service.AdminService;
import com.muhammadminhaz.inventorymanagementsystem.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
public class LoginController {

    private final AdminService adminService;
    private final AdminDAO adminDAO;
    private LoginService loginService;

    public LoginController(LoginService loginService, AdminService adminService, AdminDAO adminDAO) {
        this.loginService = loginService;
        this.adminService = adminService;
        this.adminDAO = adminDAO;
    }

    @GetMapping("/login")
    public String login() {
        return "home";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        Admin admin = adminService.getCurrentAdmin();
        model.addAttribute("admin", admin);
        return "admin_profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute Admin admin, @RequestParam("logoPath") MultipartFile file, Model model) {
        Admin currentAdmin = adminService.getCurrentAdmin();
        currentAdmin.setDisplayName(admin.getDisplayName());
        currentAdmin.setAddress(admin.getAddress());
        currentAdmin.setPhone(admin.getPhone());

        if (!file.isEmpty()) {
            try {
                String contentType = file.getContentType();
                if (contentType == null || !contentType.startsWith("image/")) {
                    model.addAttribute("error", "Invalid file type. Please upload an image.");
                    return "admin_profile";
                }

                String uploadDir = "uploads/admin_logos/";
                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);

                String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
                String fileName = currentAdmin.getUsername() + fileExtension;

                Path filePath = uploadPath.resolve(fileName);
                Files.write(filePath, file.getBytes());

                currentAdmin.setLogo(fileName);

            } catch (IOException e) {
                model.addAttribute("error", "Failed to upload the image.");
                return "admin_profile";
            }
        }

        adminService.saveAdmin(currentAdmin);
        model.addAttribute("admin", currentAdmin);
        return "redirect:/profile";
    }


    @PostMapping("/login")
    public String handleLogin(Admin admin, Model model) {
        if (loginService.authenticate(admin.getUsername(), admin.getPassword())) {
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "home";
        }
    }
}
