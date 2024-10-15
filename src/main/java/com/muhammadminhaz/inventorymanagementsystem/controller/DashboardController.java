package com.muhammadminhaz.inventorymanagementsystem.controller;

import com.muhammadminhaz.inventorymanagementsystem.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;
import java.util.Map;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Map<String, Object> salesData = dashboardService.salesData();
        Object sales = salesData.get("sales");
        Object dates = salesData.get("dates");

        model.addAttribute("salesData", sales);
        model.addAttribute("dates", dates);

        return "dashboard";
    }
}
