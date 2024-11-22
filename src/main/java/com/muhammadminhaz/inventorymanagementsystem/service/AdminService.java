package com.muhammadminhaz.inventorymanagementsystem.service;

import com.muhammadminhaz.inventorymanagementsystem.dao.AdminDAO;
import com.muhammadminhaz.inventorymanagementsystem.model.Admin;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AdminService implements UserDetailsService {

    private final AdminDAO adminDAO;
    private final PasswordEncoder passwordEncoder;

    public AdminService(AdminDAO adminDAO, PasswordEncoder passwordEncoder) {
        this.adminDAO = adminDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Admin admin = adminDAO.findByUsername(username);
        if (admin == null) {
            throw new UsernameNotFoundException("Admin not found");
        }
        return User.builder()
                .username(admin.getUsername())
                .password(admin.getPassword())
                .roles("ADMIN")
                .build();
    }

    public Admin getCurrentAdmin() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return adminDAO.findByUsername(username);
        }
        return null;
    }

    @Transactional
    public void signUp(Admin admin) {
        if (adminDAO.findByUsername(admin.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exists!");
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        adminDAO.save(admin);
    }

    public void saveAdmin(Admin admin) {
        adminDAO.save(admin);
    }

}
