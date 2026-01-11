package com.secj3303.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.secj3303.dao.Mood.MoodDaoHibernate;
import com.secj3303.dao.activity.ActivityDaoHibernate;
import com.secj3303.dao.user.UserDaoHibernate;
import com.secj3303.model.ActivityLog;
import com.secj3303.model.Feedback;
import com.secj3303.model.MoodEntry;
import com.secj3303.model.User;

@Controller
@RequestMapping("/student")
public class StudentController {

    // --- FIX: Initialize DAOs manually to prevent NullPointerException ---
    private UserDaoHibernate userDao = new UserDaoHibernate();
    private MoodDaoHibernate moodDao = new MoodDaoHibernate();
    private ActivityDaoHibernate activityDao = new ActivityDaoHibernate();

    // --- DASHBOARD ---
    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";

        User user = (User) session.getAttribute("user");
        int userId = user.getUserID();

        // Fetch Real Data
        long moodCount = moodDao.getWeeklyMoodCount(userId);
        long completedCount = activityDao.getWeeklyCompletedCount(userId);
        List<ActivityLog> logs = activityDao.getRecentActivities(userId);

        // Convert to DTO
        List<ActivityDTO> recentActivities = new ArrayList<>();
        if (logs != null) {
            for (ActivityLog log : logs) {
                String timeDisplay = log.getDate() != null ? "Just now" : "Recently";
                recentActivities.add(new ActivityDTO(log.getType(), log.getTitle(), timeDisplay));
            }
        }

        model.addAttribute("moodCount", moodCount);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("recentActivities", recentActivities);

        return "Student-home";
    }

    // --- RESOURCES ---
    @GetMapping("/activities")
    public String showActivities(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Activities";
    }

    // ===========================
    //       MOOD TRACKER
    // ===========================

    @GetMapping("/mood-tracker")
    public String showMoodTracker(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";

        User user = (User) session.getAttribute("user");

        // 1. Fetch Recent Moods
        List<MoodEntry> moodList = moodDao.getRecentMoods(user.getUserID());
        model.addAttribute("moodList", moodList);

        // 2. Prepare Data for Graph
        List<MoodEntry> graphData = new ArrayList<>();
        if (moodList != null) {
            graphData = moodList.stream().limit(7).collect(Collectors.toList());
        }
        Collections.reverse(graphData);

        List<Integer> scores = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE");

        for (MoodEntry m : graphData) {
            scores.add(m.getScore());
            if (m.getDate() != null) {
                dates.add(m.getDate().format(formatter));
            } else {
                dates.add("N/A");
            }
        }

        model.addAttribute("graphScores", scores);
        model.addAttribute("graphDates", dates);

        // Calculate Average
        double average = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        model.addAttribute("weeklyAverage", String.format("%.1f", average));

        return "Student-Mood-Tracker";
    }

    @PostMapping("/mood-tracker/save")
    public String saveMood(HttpSession session,
                           @RequestParam("mood") String mood,
                           @RequestParam("score") int score,
                           @RequestParam("note") String note) {
        if (!isStudent(session)) return "redirect:/login";

        User user = (User) session.getAttribute("user");

        MoodEntry entry = new MoodEntry(user, mood, score, note, LocalDateTime.now());
        moodDao.saveMood(entry);

        return "redirect:/student/mood-tracker";
    }

    // --- ASSESSMENT ---

    @GetMapping("/assessment")
    public String showAssessment(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Assessment";
    }

    @PostMapping("/assessment/submit")
    public String submitAssessment(HttpSession session, @RequestParam Map<String, String> answers, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        int totalScore = 0;
        int maxScore = 25; 

        for (String key : answers.keySet()) {
            if (key.startsWith("q")) {
                try {
                    totalScore += Integer.parseInt(answers.get(key));
                } catch (NumberFormatException e) { /* Ignore */ }
            }
        }

        int percentage = (int) (((double) totalScore / maxScore) * 100);

        String severityTitle;
        String severityDesc;

        if (percentage <= 40) {
            severityTitle = "Excellent Wellness Level";
            severityDesc = "You are managing your mental health very well.";
        } else if (percentage <= 70) {
            severityTitle = "Good Wellness Level";
            severityDesc = "You're doing well, but there are some areas for improvement.";
        } else {
            severityTitle = "Attention Needed";
            severityDesc = "Your responses indicate high stress levels. Consider reaching out.";
        }

        model.addAttribute("score", totalScore);
        model.addAttribute("maxScore", maxScore);
        model.addAttribute("percentage", percentage);
        model.addAttribute("severityTitle", severityTitle);
        model.addAttribute("severityDesc", severityDesc);

        return "Student-Assessment-Result";
    }

    @GetMapping("/assessment/results")
    public String showAssessmentResults(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Assessment-Result"; // Fixed file name to match previous steps
    }

    // ===========================
    //       EMERGENCY HELP
    // ===========================

    @GetMapping("/emergency")
    public String showEmergencyHelp(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";

        // 1. Fetch Users
        // Since we initialized userDao manually at the top, this will NOT be null anymore.
        List<User> dbUsers = userDao.findUsersByRole("PROFESSIONAL");

        List<Map<String, Object>> doctors = new ArrayList<>();
        String[] gradients = {
            "linear-gradient(135deg, #51A2FF 0%, #615FFF 100%)",
            "linear-gradient(135deg, #FF9A9E 0%, #FECFEF 100%)",
            "linear-gradient(135deg, #a18cd1 0%, #fbc2eb 100%)",
            "linear-gradient(135deg, #84fab0 0%, #8fd3f4 100%)"
        };
        String[] specializations = { "Licensed Psychologist", "Clinical Counselor", "Student Therapist", "Mental Health Specialist" };

        if (dbUsers != null) {
            int i = 0;
            for (User u : dbUsers) {
                Map<String, Object> doc = new HashMap<>();
                doc.put("name", "Dr. " + u.getName());
                doc.put("email", u.getEmail());
                doc.put("initials", getInitials(u.getName()));
                doc.put("gradient", gradients[i % gradients.length]);
                doc.put("role", specializations[i % specializations.length]);
                doc.put("availability", "Available Today");
                doc.put("isAvailable", true);
                doctors.add(doc);
                i++;
            }
        }

        model.addAttribute("doctors", doctors);
        return "Student-Emergency-Help";
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "DR";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1 && parts[0].length() >= 2) {
            return parts[0].substring(0, 2).toUpperCase();
        } else if (parts.length >= 2) {
            return (parts[0].substring(0, 1) + parts[1].substring(0, 1)).toUpperCase();
        }
        return "DR";
    }

    // --- FORUM ---
    @GetMapping("/forum")
    public String showForum(HttpSession session) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-forum";
    }

    // --- FEEDBACK ---
    @GetMapping("/feedback")
    public String showFeedback(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        return "Student-Feedback";
    }

    @PostMapping("/feedback/submit")
    public String submitFeedback(HttpSession session,
                                 @RequestParam("rating") int rating,
                                 @RequestParam("category") String category,
                                 @RequestParam("comments") String comments,
                                 RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        Feedback feedback = new Feedback();
        feedback.setUser(user);
        feedback.setRating(rating);
        feedback.setCategory(category);
        feedback.setComments(comments);
        feedback.setDateSubmitted(LocalDateTime.now());

        // feedbackDao.save(feedback); // Enable this once FeedbackDao is ready

        redirectAttributes.addFlashAttribute("successMessage", "Thank you! Your feedback helps us improve.");
        return "redirect:/student/feedback";
    }

    // --- HELPER ---
    private boolean isStudent(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "STUDENT".equalsIgnoreCase(user.getRole());
    }

    // --- DTO CLASS ---
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