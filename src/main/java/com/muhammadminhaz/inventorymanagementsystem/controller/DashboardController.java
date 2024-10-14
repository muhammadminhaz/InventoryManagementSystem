package com.muhammadminhaz.inventorymanagementsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Sample data for Highcharts (replace with actual data from your service)
        int[] salesData = {1500, 2300, 1800, 3000, 2500, 4000}; // Example sales data
        String[] dates = {"2024-10-01", "2024-10-02", "2024-10-03", "2024-10-04", "2024-10-05", "2024-10-06"}; // Example dates

        model.addAttribute("salesData", salesData);
        model.addAttribute("dates", dates);

        return "dashboard"; // This will return the dashboard.html
    }
}
