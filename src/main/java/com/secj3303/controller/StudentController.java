package com.secj3303.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secj3303.model.User;

@Controller
@RequestMapping("/student")
public class StudentController {

    @GetMapping("/home")
    public String showStudentDashboard(HttpSession session, Model model) {
        // 1. Security Check
        User user = (User) session.getAttribute("user");
        if (user == null || !"STUDENT".equals(user.getRole())) {
            return "redirect:/login";
        }

        // 2. Mock Data
        model.addAttribute("completedCount", 4);
        model.addAttribute("moodCount", 5);
        
        List<ActivityDTO> activities = new ArrayList<>();
        activities.add(new ActivityDTO("MOOD", "Logged mood: Happy", "2 hours ago"));
        activities.add(new ActivityDTO("EXERCISE", "Completed: Mindfulness", "5 hours ago"));
        activities.add(new ActivityDTO("READING", "Read: Managing Exam Stress", "Yesterday"));
        
        model.addAttribute("recentActivities", activities);


        return "Student-home"; 
    }

    // (Keep your ActivityDTO class here) (this is temp and will be deleted)
    public class ActivityDTO {
        private String type;
        private String title;
        private String timeAgo;

        public ActivityDTO(String type, String title, String timeAgo) {
            this.type = type;
            this.title = title;
            this.timeAgo = timeAgo;
        }

        public String getType() { return type; }
        public String getTitle() { return title; }
        public String getTimeAgo() { return timeAgo; }
    }
}