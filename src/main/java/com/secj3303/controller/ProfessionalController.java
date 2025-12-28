package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/professional")
public class ProfessionalController {

    @GetMapping("/home")
    public String showHome(HttpSession session) {
        if (!isProfessional(session)) return "redirect:/login";
        return "Professional-home"; // Maps to Professional-home.html
    }

    @GetMapping("/forum")
    public String showForum(HttpSession session) {
        if (!isProfessional(session)) return "redirect:/login";
        return "Professional-forum"; 
    }

    @GetMapping("/resources/upload")
    public String showUploadResources(HttpSession session) {
        if (!isProfessional(session)) return "redirect:/login";
        return "Professional-upload-Resuorces"; 
    }

    // Helper to secure professional routes
    private boolean isProfessional(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "PROFESSIONAL".equalsIgnoreCase(role);
    }
}