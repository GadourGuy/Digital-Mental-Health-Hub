package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/home")
    public String showHome(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "Admin-home"; // Maps to Admin-home.html
    }

    @GetMapping("/forum")
    public String showForum(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "Admin-Forum"; 
    }

    @GetMapping("/panel")
    public String showAdminPanel(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        return "Admin-adminPanel"; 
    }

    // Helper to secure admin routes
    private boolean isAdmin(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "ADMIN".equalsIgnoreCase(role);
    }
}