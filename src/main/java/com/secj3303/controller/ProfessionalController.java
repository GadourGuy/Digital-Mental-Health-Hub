package com.secj3303.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.secj3303.dao.content.ContentDao;
import com.secj3303.dao.user.UserDao;
import com.secj3303.model.Category;
import com.secj3303.model.User;

@Controller
@RequestMapping("/professional")
public class ProfessionalController {

    @Autowired
    private ContentDao contentDao; 
    @Autowired
    private UserDao userDao;

    // this route will return the home page that returns the professional object, along with number of pending resources to update..

    // along with engagement rate that shows out of all the students in the system, how many of them engaged in the content uploaded by the professional
    @GetMapping("/home")
    public String showHome(HttpSession session) {
        // if (!isProfessional(session)) return "redirect:/login";
        // get the info from the session
        // String name = (String) session.getAttribute("name");
        // String email = (String) session.getAttribute("email");
        // String role = (String) session.getAttribute("role");
        // int id = (Integer) session.getAttribute("id");

        // int pendingContent = contentDao.getPendingContent(id);
        // int contentCompleted = contentDao.GetProfessionalCompletedContent(id);
        // int numOfStudents = UserDao.getTotalUsers();

        // double contentCompletedPercentage = ((contentCompleted * 1.0) / numOfStudents) * 100;

        // to return:
        // professional (id, name, email) in session no need to return
        // pending content
        // contentCompletedPercentage




        return "Professional-home"; // Maps to Professional-home.html
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
        
        //SubContent subContent = new SubContent(title, categoryObj, description, url, professional);
        
       // contentDao.uploadContent(subContent);
        
        return "redirect:/professional/resources/upload"; 
    }

    // Helper to secure professional routes
    private boolean isProfessional(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "PROFESSIONAL".equalsIgnoreCase(role);
    }
}