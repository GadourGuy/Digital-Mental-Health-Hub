package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Maps to login.html
    }

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup"; // Maps to signup.html
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
    
    // Placeholder for actual login logic
    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, @RequestParam String password, HttpSession session) {
        // TODO: Validate user against Database here
        // For now, this is just a placeholder example:
        if (email.contains("student")) {
            session.setAttribute("role", "STUDENT");
            return "redirect:/student/home";
        } else if (email.contains("prof")) {
            session.setAttribute("role", "PROFESSIONAL");
            return "redirect:/professional/home";
        } else if (email.contains("admin")) {
            session.setAttribute("role", "ADMIN");
            return "redirect:/admin/home";
        }
        return "redirect:/login?error=true";
    }
}