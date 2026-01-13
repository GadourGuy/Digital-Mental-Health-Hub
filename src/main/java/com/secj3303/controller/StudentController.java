package com.secj3303.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.secj3303.dao.Mood.MoodDaoHibernate;
import com.secj3303.dao.activity.ActivityDaoHibernate;
import com.secj3303.dao.assessment.AssessmentDao;
import com.secj3303.dao.content.ContentDaoHibernate;
import com.secj3303.dao.feedback.FeedbackDao;
import com.secj3303.dao.user.UserDaoHibernate;
import com.secj3303.model.ActivityLog;
import com.secj3303.model.AssessmentEntry;
import com.secj3303.model.Feedback;
import com.secj3303.model.MoodEntry;
import com.secj3303.model.SubContent;
import com.secj3303.model.User;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired private UserDaoHibernate userDao;
    @Autowired private MoodDaoHibernate moodDao;
    @Autowired private ActivityDaoHibernate activityDao;
    @Autowired private ContentDaoHibernate contentDao; 
    @Autowired private FeedbackDao feedbackDao;
    @Autowired private AssessmentDao assessmentDao;
    // --- DASHBOARD ---
    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        User user = (User) session.getAttribute("user");
        
        long moodCount = moodDao.getWeeklyMoodCount(user.getUserID());
        long completedCount = activityDao.getWeeklyCompletedCount(user.getUserID());
        List<ActivityLog> logs = activityDao.getRecentActivities(user.getUserID());

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

    // --- RESOURCES & ACTIVITIES ---
    @GetMapping("/activities")
    public String showActivities(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        User user = (User) session.getAttribute("user");

        // 1. Weekly Schedule Logic
        List<WeeklyTaskDTO> weeklyTasks = getWeeklySchedule();
        List<ActivityLog> weeklyLogs = activityDao.getWeeklyLogs(user.getUserID());
        
        int completedCount = 0;
        for (WeeklyTaskDTO task : weeklyTasks) {
            boolean isDone = false;
            if (weeklyLogs != null) {
                // Check if any log in the DB has the SAME title as the task
                isDone = weeklyLogs.stream()
                    .anyMatch(log -> log.getTitle() != null && log.getTitle().equalsIgnoreCase(task.getTitle()));
            }
            task.setCompleted(isDone);
            if(isDone) completedCount++;
        }

        model.addAttribute("weeklyTasks", weeklyTasks);
        model.addAttribute("completedCount", completedCount);
        model.addAttribute("totalTasks", weeklyTasks.size());
        int progress = weeklyTasks.size() > 0 ? (int)(((double)completedCount / weeklyTasks.size()) * 100) : 0;
        model.addAttribute("progressPercentage", progress);

        // 2. Resource Library
        List<SubContent> allContent = contentDao.getAllSubContents(); 
        List<SubContent> articles = new ArrayList<>();
        List<SubContent> videos = new ArrayList<>();
        List<SubContent> selfHelp = new ArrayList<>();

        if (allContent != null) {
            for (SubContent c : allContent) {
                if ("Approved".equalsIgnoreCase(c.getStatus())) {
                    String type = c.getType() != null ? c.getType().toLowerCase() : "";
                    if (type.contains("article")) articles.add(c);
                    else if (type.contains("video")) videos.add(c);
                    else selfHelp.add(c);
                }
            }
        }

        model.addAttribute("articles", articles);
        model.addAttribute("videos", videos);
        model.addAttribute("selfHelp", selfHelp);

        return "Student-Activities";
    }

    // --- NEW ACTION: Click to Complete Task ---
    @GetMapping("/activities/complete")
    public String completeTask(HttpSession session, @RequestParam("title") String title) {
        if (!isStudent(session)) return "redirect:/login";
        User user = (User) session.getAttribute("user");

        // Create a new log entry when user clicks the circle
        ActivityLog log = new ActivityLog();
        log.setUser(user);
        log.setType("WELLNESS_CHALLENGE");
        log.setTitle(title); // Saves "Gratitude Journaling", "5-Minute Meditation", etc.
        log.setStatus("COMPLETED");
        log.setDate(LocalDateTime.now());
        
        // Save to DB
        activityDao.save(log);

        return "redirect:/student/activities";
    }

    // Helper for Static Schedule
    private List<WeeklyTaskDTO> getWeeklySchedule() {
        List<WeeklyTaskDTO> tasks = new ArrayList<>();
        tasks.add(new WeeklyTaskDTO("Monday", "Gratitude Journaling", "Write down 3 things you're grateful for"));
        tasks.add(new WeeklyTaskDTO("Tuesday", "5-Minute Meditation", "Practice mindful breathing"));
        tasks.add(new WeeklyTaskDTO("Wednesday", "Physical Activity", "30 minutes of movement"));
        tasks.add(new WeeklyTaskDTO("Thursday", "Social Connection", "Reach out to a friend"));
        tasks.add(new WeeklyTaskDTO("Friday", "Digital Detox Hour", "One hour without screens"));
        tasks.add(new WeeklyTaskDTO("Saturday", "Self-Care Activity", "Do something that brings you joy"));
        tasks.add(new WeeklyTaskDTO("Sunday", "Weekly Reflection", "Reflect on your week"));
        return tasks;
    }

    // --- MOOD TRACKER ---
    @GetMapping("/mood-tracker")
    public String showMoodTracker(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        User user = (User) session.getAttribute("user");
        List<MoodEntry> moodList = moodDao.getRecentMoods(user.getUserID());
        model.addAttribute("moodList", moodList);

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
            dates.add(m.getDate() != null ? m.getDate().format(formatter) : "N/A");
        }

        model.addAttribute("graphScores", scores);
        model.addAttribute("graphDates", dates);
        
        double average = scores.stream().mapToInt(Integer::intValue).average().orElse(0.0);
        model.addAttribute("weeklyAverage", String.format("%.1f", average));
        return "Student-Mood-Tracker";
    }

    @PostMapping("/mood-tracker/save")
    public String saveMood(HttpSession session, @RequestParam("mood") String mood, 
                           @RequestParam("score") int score, @RequestParam("note") String note) {
        if (!isStudent(session)) return "redirect:/login";
        User user = (User) session.getAttribute("user");
        MoodEntry entry = new MoodEntry(user, mood, score, note, LocalDateTime.now());
        moodDao.saveMood(entry);
        return "redirect:/student/mood-tracker";
    }

    // --- OTHER PAGES ---
    @GetMapping("/assessment")
    public String showAssessment(HttpSession session) { return isStudent(session) ? "Student-Assessment" : "redirect:/login"; }

    @PostMapping("/assessment/submit")
    public String submitAssessment(HttpSession session, @RequestParam Map<String, String> answers, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        int totalScore = 0;
        for (String key : answers.keySet()) {
            if (key.startsWith("q")) {
                try { totalScore += Integer.parseInt(answers.get(key)); } catch (Exception e) {}
            }
        }
        int percentage = (int) (((double) totalScore / 25) * 100);
        String title = percentage <= 40 ? "Excellent" : (percentage <= 70 ? "Good" : "Attention Needed");
        String desc = percentage <= 40 ? "Managing well." : (percentage <= 70 ? "Doing okay." : "High stress.");

        User user = (User) session.getAttribute("user");
        AssessmentEntry entry = new AssessmentEntry();
        entry.setUser(user);
        entry.setTotalScore(totalScore);
        entry.setSeverity(title); // Saving "Good", "Excellent", etc.
        entry.setDate(LocalDateTime.now());
        
        assessmentDao.saveAssessment(entry);
        model.addAttribute("score", totalScore);
        model.addAttribute("percentage", percentage);
        model.addAttribute("severityTitle", title);
        model.addAttribute("severityDesc", desc);
        model.addAttribute("maxScore", 25);
        return "Student-Assessment-Result";
    }

    @GetMapping("/emergency")
    public String showEmergency(HttpSession session, Model model) {
        if (!isStudent(session)) return "redirect:/login";
        List<User> dbUsers = userDao.findUsersByRole("PROFESSIONAL");
        List<Map<String, Object>> doctors = new ArrayList<>();
        // ... (Keep your existing doctor mapping logic if you want) ...
        model.addAttribute("doctors", doctors);
        return "Student-Emergency-Help";
    }
    
    @GetMapping("/forum")
    public String showForum(HttpSession session) { return isStudent(session) ? "Student-forum" : "redirect:/login"; }
    
    @GetMapping("/feedback")
    public String showFeedback(HttpSession session) { return isStudent(session) ? "Student-Feedback" : "redirect:/login"; }

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
        redirectAttributes.addFlashAttribute("successMessage", "Thank you! Your feedback helps us improve.");
        feedbackDao.saveFeedback(feedback);
        return "redirect:/student/feedback";
    }

    private boolean isStudent(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "STUDENT".equalsIgnoreCase(user.getRole());
    }

    // DTOs
    public class ActivityDTO {
        public String type, title, timeAgo;
        public ActivityDTO(String type, String title, String timeAgo) {
            this.type = type; this.title = title; this.timeAgo = timeAgo;
        }
    }

    public class WeeklyTaskDTO {
        public String day, title, description;
        public boolean isCompleted;
        public WeeklyTaskDTO(String d, String t, String desc) {
            this.day = d; this.title = t; this.description = desc; this.isCompleted = false;
        }
        public void setCompleted(boolean c) { this.isCompleted = c; }
        public String getTitle() { return title; }
        public String getDay() { return day; }
        public String getDescription() { return description; }
        public boolean isCompleted() { return isCompleted; }
    }
}