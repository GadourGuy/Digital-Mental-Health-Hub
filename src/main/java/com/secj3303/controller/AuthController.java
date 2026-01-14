package com.secj3303.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    
    // Inject PasswordEncoder defined in SecurityConfig
    @Autowired
    private PasswordEncoder passwordEncoder;

    // --- Login Routes ---
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; 
    }


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
        User newUser = new User();
        newUser.setName(fullName); 
        newUser.setEmail(email);
        
        // Hash the password before saving
        String encodedPassword = passwordEncoder.encode(password);
        newUser.setPassword(encodedPassword);
        
        // 4. AUTOMATICALLY set role to STUDENT
        newUser.setRole("STUDENT");

        // 5. Save to Database
        userDao.insertUser(newUser);

        // 6. Redirect to login page on success
        return "redirect:/login?success=true";
    }
}