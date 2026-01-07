package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.user.UserDao;
import com.secj3303.model.User;

@Controller
public class AuthController {

    @Autowired
    private UserDao userDao;

    // --- Login Routes ---
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String email, 
                              @RequestParam String password, 
                              HttpSession session) {
        
        User user = userDao.getUserByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            session.setAttribute("role", user.getRole());

            if ("STUDENT".equalsIgnoreCase(user.getRole())) {
                return "redirect:/student/home";
            } else if ("PROFESSIONAL".equalsIgnoreCase(user.getRole())) {
                return "redirect:/professional/home";
            } else if ("ADMIN".equalsIgnoreCase(user.getRole())) {
                return "redirect:/admin/home";
            }
            return "redirect:/home";
        } 
        return "redirect:/login?error=true";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // --- NEW SIGNUP ROUTES ---

    @GetMapping("/signup")
    public String showSignupForm() {
        return "signup"; // Maps to signup.html
    }

    @PostMapping("/signup")
    public String handleSignup(@RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword) {
        
        // 1. Basic Validation: Check if passwords match
        if (!password.equals(confirmPassword)) {
            return "redirect:/signup?error=passwords_mismatch";
        }

        // 2. Check if email already exists
        if (userDao.getUserByEmail(email) != null) {
            return "redirect:/signup?error=email_exists";
        }

        // 3. Create new User Object
        // (Assuming your User model has setters for these fields)
        User newUser = new User();
        newUser.setName(fullName); // Make sure your User model has this field
        newUser.setEmail(email);
        newUser.setPassword(password);
        
        // 4. AUTOMATICALLY set role to STUDENT
        newUser.setRole("STUDENT");

        // 5. Save to Database
        userDao.insertUser(newUser);

        // 6. Redirect to login page on success
        return "redirect:/login?success=true";
    }
}