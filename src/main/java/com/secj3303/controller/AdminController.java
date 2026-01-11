package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secj3303.dao.admin.AdminDao;
import com.secj3303.dao.content.ContentDao;
import com.secj3303.dao.professional.ProfessionalDao;
import com.secj3303.model.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ContentDao contentDao; 
    

    @Autowired
    private ProfessionalDao professionalDao;

    @Autowired
    private AdminDao adminDao;

    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        User admin = (User) session.getAttribute("user");
        int id = admin.getUserID();

        int pendingContent = contentDao.getAllPendingContent();
        int numberOfProfessionals = adminDao.getAllProfessionals();
        int numOfStudents = professionalDao.getStudents();

        int pendingProfessional = adminDao.getProfessionalRequests();


        // to return:
        model.addAttribute("admin", admin);
        // pending content
        model.addAttribute("pendingContent", pendingContent);

        // number of professionals in the system
        model.addAttribute("numberOfProfessionals", numberOfProfessionals);
        
        // number of students
        model.addAttribute("numberOfStudents", numOfStudents);
        
        // number of users request for being professionals
        model.addAttribute("pendingProfessional", pendingProfessional);


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