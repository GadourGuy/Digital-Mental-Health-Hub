package com.secj3303.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student")
public class StudentController {

    // ===========================
    //       DASHBOARD & HOME
    // ===========================
    @GetMapping("/home")
    public String showHome(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-home"; // Maps to Student-home.html
    }

    // ===========================
    //       FEATURES
    // ===========================
    @GetMapping("/activities")
    public String showActivities(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Activities"; 
    }

    @GetMapping("/mood-tracker")
    public String showMoodTracker(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Mood-Tracker"; 
    }

    @GetMapping("/assessment")
    public String showAssessment(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Self-Assesment"; 
    }
    
    @GetMapping("/assessment/results")
    public String showAssessmentResults(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Assesment-Results"; 
    }

    @GetMapping("/chatbot")
    public String showChatbot(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Chatbot"; 
    }

    @GetMapping("/forum")
    public String showForum(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-forum"; 
    }

    @GetMapping("/feedback")
    public String showFeedback(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Feedback"; 
    }

    @GetMapping("/emergency")
    public String showEmergencyHelp(HttpSession session) {
        // Usually emergency pages might be public, but if restricted:
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Emergency-Help"; 
    }

    // Helper to secure student routes
    private boolean isStudent(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "STUDENT".equalsIgnoreCase(role);
    }
}