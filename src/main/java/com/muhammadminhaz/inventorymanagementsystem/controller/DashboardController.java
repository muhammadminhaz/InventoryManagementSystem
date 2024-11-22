package com.muhammadminhaz.inventorymanagementsystem.controller;

import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import com.muhammadminhaz.inventorymanagementsystem.service.AdminService;
import com.muhammadminhaz.inventorymanagementsystem.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.Map;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final AdminService adminService;

    public DashboardController(DashboardService dashboardService, AdminService adminService) {
        this.dashboardService = dashboardService;
        this.adminService = adminService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Admin admin = adminService.getCurrentAdmin();
        Map<String, Object> salesData = dashboardService.salesData(admin);
        Object sales = salesData.get("sales");
        Object dates = salesData.get("dates");

        model.addAttribute("salesData", sales);
        model.addAttribute("dates", dates);

        return "dashboard";
    }
}
