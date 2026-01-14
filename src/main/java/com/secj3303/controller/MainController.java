package com.secj3303.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    // 1. Root Path
    @GetMapping("/")
    public String root(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            for (GrantedAuthority auth : authentication.getAuthorities()) {
                String role = auth.getAuthority();
                if (role.equals("ROLE_ADMIN")) return "redirect:/admin/home";
                if (role.equals("ROLE_STUDENT")) return "redirect:/student/home";
                if (role.equals("ROLE_PROFESSIONAL")) return "redirect:/professional/home";
            }
            return "redirect:/home"; 
        }
        return "redirect:/login";
    }

    // 2. Access Denied (Matches web.xml 403)
    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error403"; 
    }

    // 3. Page Not Found (Matches web.xml 404)
    @GetMapping("/error-404")
    public String error404() {
        return "error404"; 
    }
}