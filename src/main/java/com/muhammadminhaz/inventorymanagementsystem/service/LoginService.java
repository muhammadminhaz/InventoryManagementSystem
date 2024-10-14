package com.muhammadminhaz.inventorymanagementsystem.service;

import com.muhammadminhaz.inventorymanagementsystem.dao.AdminDAO;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private AdminDAO adminDAO;

    public LoginService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public boolean authenticate(String username, String password) {
        if (adminDAO.findByUsernameAndPassword(username, password).isPresent()) {
            return true;
        }
        return false;
    }
}
