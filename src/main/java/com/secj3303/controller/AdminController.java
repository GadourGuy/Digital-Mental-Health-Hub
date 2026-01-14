package com.secj3303.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.secj3303.dao.Mood.MoodDao;
import com.secj3303.dao.admin.AdminDao;
import com.secj3303.dao.content.ContentDao;
import com.secj3303.dao.feedback.FeedbackDao;
import com.secj3303.dao.forum.ForumPostDao;
import com.secj3303.dao.professional.ProfessionalDao;
import com.secj3303.dao.student.StudentDao;
import com.secj3303.dao.user.UserDao;
import com.secj3303.model.Feedback;
import com.secj3303.model.ForumPost;
import com.secj3303.model.MoodEntry;
import com.secj3303.model.ProfessionalRequest;
import com.secj3303.model.SubContent;
import com.secj3303.model.User;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private ContentDao contentDao; 

    @Autowired
    private UserDao userDao;

    @Autowired
    private ProfessionalDao professionalDao;

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private StudentDao studentDao;

    @Autowired
    private ForumPostDao forumPostDao;

    @Autowired
    private MoodDao moodDao;

    @Autowired
    private FeedbackDao feedbackDao;

    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        User admin = (User) session.getAttribute("user");
        // int id = admin.getUserID();

        int pendingContent = contentDao.getAllPendingContent();
        int numberOfProfessionals = professionalDao.getAllProfessionals();
        int numOfStudents = studentDao.getNumOfStudents();
        int pendingProfessional = professionalDao.getProfessionalRequests();

// *****************************************************************
// to do: add counters to num of posts
        // category with highest engage,emt, content with highest engagement and by who, professional with highest engagement
// *****************************************************************
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


        return "admin-home"; // Maps to Admin-home.html
    }


    @GetMapping("/professional-requests")
    public String viewResourcesUploads(HttpSession session, Model model) {
        List<ProfessionalRequest> requests = professionalDao.getAllPendingProfessionalRequests();
        model.addAttribute("requests", requests);

        return "admin-view-professional-requests";
    }


    @GetMapping("/manage-request")
    public String getUserRequest(HttpSession session, Model model, @RequestParam("requestID") int requestID) {
        ProfessionalRequest request = professionalDao.getSingleProfessionalRequest(requestID);
        model.addAttribute("req", request);
        return "admin-manage-professional-request";
    }

    @PostMapping("/manage-request")
    public String changeProfessionalRequestStatus(@RequestParam("id") int requestID, 
            @RequestParam("status") String status, 
            @RequestParam(value = "message", required = false) String message,
            HttpSession session, 
            RedirectAttributes redirectAttributes) {

        if ("rejected".equalsIgnoreCase(status)) {
            if (message == null || message.trim().isEmpty()) {
                
                redirectAttributes.addFlashAttribute("error", "Rejection reason is required when rejecting a request.");
                
                return "redirect:/admin/manage-request?requestID=" + requestID;
            }
        }

        adminDao.editPendingRequest(requestID, status, message);

        redirectAttributes.addFlashAttribute("success", "Request successfully " + status + ".");

        return "redirect:/admin/manage-request?requestID=" + requestID;
    } 



    // Resources
    @GetMapping("/resources-requests")
    public String getPendingResources(HttpSession session, Model model) {
        List<SubContent> uploadedContent = adminDao.getAllPendingContent();
        model.addAttribute("uploadedContent", uploadedContent);

        return "admin-view-professional-resources";
    }

    // one resource
    @GetMapping("/resources/manage")
    public String getSingleResource(HttpSession session, Model model, @RequestParam("contentID") int contentID) {
        SubContent content = contentDao.getSubContentByID(contentID);

        model.addAttribute("content", content);
        return "resource page";
    }
    
    @PostMapping("/resources/manage")
    public String changeProfessionalContentStatus(@RequestParam("id") int contentID, 
            @RequestParam("status") String status, 
            @RequestParam(value = "message", required = false) String message,
            HttpSession session, 
            RedirectAttributes redirectAttributes) {

        if ("rejected".equalsIgnoreCase(status)) {
            if (message == null || message.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Rejection reason is required when rejecting a request.");
                return "redirect:/admin/resources/manage?contentID=" + contentID;
            }
        }

        contentDao.changeProfessionalContentStatus(contentID, status, message);
        redirectAttributes.addFlashAttribute("success", "Request successfully " + status + ".");

        return "redirect:/admin/resources/manage?contentID=" + contentID;
    } 

// Monitor students
    @GetMapping("/monitor/students")
    public String showMonitorStudents(HttpSession session, Model model) {
        List<User> students = studentDao.getAllStudents();
        List<MoodEntry> studentsMoods = moodDao.getUsersMood();

        Map<Integer, MoodEntry> moodMap = new HashMap<>();
        for (MoodEntry mood : studentsMoods) {
            moodMap.put(mood.getUser().getUserID(), mood);
        }
        List<MoodEntry> orderedStudentMoods = new ArrayList<>();
        for (User student : students) {
            MoodEntry mood = moodMap.get(student.getUserID());
            orderedStudentMoods.add(mood);
        }

        model.addAttribute("students", students);
        model.addAttribute("studentMoods", orderedStudentMoods);

        return "admin-monitor-students"; 
    }

    @GetMapping("/manage-student")
    String showStudentPage(HttpSession session, Model model, @RequestParam("studentID") int studentID) {        
        User student = userDao.getUser(studentID);
        List<ForumPost> userPosts = forumPostDao.getPostsByUserId(studentID);
        int completedResoucesCount = adminDao.getCompletedResourcesCount(studentID);
        List<MoodEntry> userMood = moodDao.getRecentMoods(studentID);
        List<Feedback> sutdentFeedbacks = feedbackDao.getUserFeedback(studentID);

        model.addAttribute("student", student);
        model.addAttribute("userPosts", userPosts);
        model.addAttribute("userMood", userMood);
        model.addAttribute("completedResoucesCount", completedResoucesCount);
        model.addAttribute("feedbacks", sutdentFeedbacks);

        return "admin-manage-students";
    }


    
    @GetMapping("/feedback")
    public String showfeedbacks(HttpSession session, Model model) {
        List<Feedback> feedbacks = feedbackDao.getAllFeedbacks();
        model.addAttribute("feedbacks", feedbacks);

        return "admin-feedback"; 
    }

    @GetMapping("/resources")
    public String showResources(HttpSession session, Model model) {
        List<SubContent> allContent = contentDao.getAllSubContents(); 
        List<SubContent> articles = new ArrayList<>();
        List<SubContent> videos = new ArrayList<>();
        List<SubContent> selfHelp = new ArrayList<>();

        if (allContent != null) {
            for (SubContent c : allContent) {
                // Admin sees "Approved" content in the library view
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
}