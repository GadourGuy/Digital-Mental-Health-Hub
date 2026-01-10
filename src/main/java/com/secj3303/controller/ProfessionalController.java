package com.secj3303.controller;

import java.util.List;
// import java.util.Locale.Category;

import javax.servlet.http.HttpSession;

import com.secj3303.dao.content.ContentDao;
import com.secj3303.dao.professional.ProfessionalDao;
import com.secj3303.dao.user.UserDao;
import com.secj3303.model.SubContent;
import com.secj3303.model.User;
import com.secj3303.model.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/professional")
public class ProfessionalController {

    @Autowired
    private ContentDao contentDao; 
    @Autowired
    private UserDao userDao;

    @Autowired
    private ProfessionalDao professionalDao;

    // this route will return the home page that returns the professional object, along with number of pending resources to update..

    // along with engagement rate that shows out of all the students in the system, how many of them engaged in the content uploaded by the professional
    @GetMapping("/home")
    public String showHome(HttpSession session, Model model) {
        if (!isProfessional(session)) return "redirect:/login";
        // get the info from the session
        User professional = (User) session.getAttribute("user");
        int id = professional.getUserID();

        int pendingContent = contentDao.getPendingContent(id);
        int contentCompleted = contentDao.GetProfessionalCompletedContent(id);
        int numOfStudents = professionalDao.getStudents();

        double contentCompletedPercentage = ((contentCompleted * 1.0) / numOfStudents) * 100;

        // to return:
        model.addAttribute("professional", professional);
        // pending content
        model.addAttribute("pendingContent", pendingContent);
        // contentCompletedPercentage
        model.addAttribute("completedPercentage", contentCompletedPercentage);
        
        // number of students
        model.addAttribute("numberOfStudents", numOfStudents);
        return "Professional-home";
    }

    @GetMapping("/forum")
    public String showForum(HttpSession session) {
        // if (!isProfessional(session)) return "redirect:/login";
        return "Professional-forum"; 
    }

    @GetMapping("/resources/upload")
    public String showUploadResources(HttpSession session, Model model) {
        // if (!isProfessional(session)) return "redirect:/login";
        
        // return the list of categories to the page
        List<Category> categories = contentDao.getContentCategories();
        model.addAttribute("categories", categories);
        
        return "Professional-upload-Resuorces"; 
    }

    // where the professional will upload the content
    @PostMapping("/resources/upload")
    public String uploadResources(@RequestParam("title") String title, @RequestParam("category") String category, @RequestParam("description") String description, @RequestParam("url") String url, HttpSession session) {

        // if (!isProfessional(session)) return "redirect:/login";
        
        int categoryID = contentDao.getCategoryID(category);
        Category categoryObj = new Category();
        categoryObj.setCategoryID(categoryID);
        categoryObj.setContentTitle(category);

        User professional = (User) session.getAttribute("user");
        
        SubContent subContent = new SubContent(title, categoryObj, description, url, professional);
        
        contentDao.uploadContent(subContent);
        
        return "redirect:/professional/resources/upload"; 
    }

    // Helper to secure professional routes
    private boolean isProfessional(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "PROFESSIONAL".equalsIgnoreCase(role);
    }
}