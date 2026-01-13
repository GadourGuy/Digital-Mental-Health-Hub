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

import com.secj3303.dao.content.ContentDao;
import com.secj3303.dao.professional.ProfessionalDao;
import com.secj3303.model.Category;
import com.secj3303.model.SubContent;
import com.secj3303.model.User;

@Controller
@RequestMapping("/professional")
public class ProfessionalController {

    @Autowired
    private ContentDao contentDao; 
    

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
        if (!isProfessional(session)) return "redirect:/login";
        return "student-forum"; 
    }

    @GetMapping("/my-resources")
    public String showResources(HttpSession session, Model model) {
        if (!isProfessional(session)) return "redirect:/login";
        User professional = (User) session.getAttribute("user");
        int id = professional.getUserID();
        List<SubContent> professionalContent = professionalDao.getUploadedResources(id);
        
        model.addAttribute("uploadedContent", professionalContent);
        return "Professional-resources";
    }

    @PostMapping("/my-resources/delete")
    public String deleteContent(HttpSession session, Model model, @RequestParam("contentID") int contentID, RedirectAttributes redirectAttributes) {
        if (!isProfessional(session)) return "redirect:/login";
        User professional = (User) session.getAttribute("user");
        SubContent subContent = contentDao.getSubContentByID(contentID);
        if(subContent.getProfessional().getUserID() != professional.getUserID()) return "redirect:/professional/home";
        contentDao.deleteContentByID(contentID);

        redirectAttributes.addFlashAttribute("success", "Resource deleted successfully!");
        return "redirect:/professional/my-resources";
    }

    @GetMapping("/my-resources/view")
    public String showSingleContent (HttpSession session, Model model, @RequestParam("contentID") int contentID) {
        if (!isProfessional(session)) return "redirect:/login";
        
        User professional = (User) session.getAttribute("user");
        SubContent subContent = contentDao.getSubContentByID(contentID);

        if(subContent.getProfessional().getUserID() != professional.getUserID()) return "redirect:/professional/home";

        model.addAttribute("subContent", subContent);
        return "professional-single-content";
    }


    @GetMapping("/resources/upload")
    public String showUploadResources(HttpSession session, Model model) {
        if (!isProfessional(session)) return "redirect:/login";
        
        // return the list of categories to the page
        List<Category> categories = contentDao.getContentCategories();
        User professional = (User) session.getAttribute("user");
        int id = professional.getUserID();


        int pendingContent = contentDao.getPendingContent(id);
        int approvedContent = contentDao.getApprovedContent(id);

        int totalContent = pendingContent + approvedContent;

        model.addAttribute("categories", categories);
        model.addAttribute("pendingContent", pendingContent);
        model.addAttribute("approvedContent", approvedContent);
        model.addAttribute("totalContent", totalContent);


        return "Professional-upload-Resources"; 
    }

    // where the professional will upload the content
    @PostMapping("/resources/upload")
    public String uploadResources(
        @RequestParam("resourceType") String type,
        @RequestParam("title") String title,
        @RequestParam("category") String category,
        @RequestParam("description") String description,
        @RequestParam("url") String url,
        HttpSession session,
        Model model,
        RedirectAttributes redirectAttributes
            ) {

        if (!isProfessional(session)) return "redirect:/login";
                
        Map<String, String> errors = new HashMap<>();
                
        // Validate resourceType
        if (type == null || type.trim().isEmpty()) {
            errors.put("resourceType" , "Resource type is required");
        } else if (!type.equals("pdf") && !type.equals("video")) {
            errors.put("resourceType", "Invalid resource type");
        }
        
        // Validate title
        if (title == null || title.trim().isEmpty()) {
            errors.put("title", "Title is required");
        } else if (title.trim().length() < 3) {
            errors.put("title", "Title must be at least 3 characters long");
        }
        
        // Validate category
        if (category == null || category.trim().isEmpty()) {
            errors.put("category", "Category is required");
        }
        
        // Validate description
        if (description == null || description.trim().isEmpty()) {
            errors.put("description", "Description is required");
        } else if (description.trim().length() < 10) {
            errors.put("description", "Description must be at least 10 characters long");
        }
        
        // Validate URL
        if (url == null || url.trim().isEmpty()) {
            errors.put("url", "URL is required");
        } else if (!url.startsWith("http://") && !url.startsWith("https://")) {
            errors.put("url", "URL must start with http:// or https://");
        }
        
        // If there are any errors, return to the form
        if (!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("title", title);
            redirectAttributes.addFlashAttribute("category", category);
            redirectAttributes.addFlashAttribute("description", description);
            redirectAttributes.addFlashAttribute("url", url);
            redirectAttributes.addFlashAttribute("resourceType", type);
            return "redirect:/professional/resources/upload";
        }
        
        User professional = (User) session.getAttribute("user");
        Category categoryObj = new Category();
        categoryObj.setCategoryID(Integer.parseInt(category));
        // int categoryID = contentDao.getCategoryID(category);
        SubContent newSubContent = new SubContent(title, categoryObj, description, url, professional, type);
        
        
        professionalDao.addContent(newSubContent);
        redirectAttributes.addFlashAttribute("success", "Resource uploaded successfully!");
        return "redirect:/professional/home";
         
    }


    // editing the resources
    @GetMapping("/resources/edit")
    public String showEditResources(HttpSession session, Model model, @RequestParam("contentID") int contentID) {
        if (!isProfessional(session)) return "redirect:/login";
        
        // return the list of categories to the page
        List<Category> categories = contentDao.getContentCategories();
        User professional = (User) session.getAttribute("user");
        SubContent subContent = contentDao.getSubContentByID(contentID);
        if(subContent.getProfessional().getUserID() != professional.getUserID()) return "redirect:/professional/home";
        
        int id = professional.getUserID();
        int pendingContent = contentDao.getPendingContent(id);
        int approvedContent = contentDao.getApprovedContent(id);

        int totalContent = pendingContent + approvedContent;

        model.addAttribute("categories", categories);
        model.addAttribute("pendingContent", pendingContent);
        model.addAttribute("approvedContent", approvedContent);
        model.addAttribute("totalContent", totalContent);
        model.addAttribute("contentToEdit", subContent);


        return "Professional-edit-resources"; 
    }

    @PostMapping("/resources/edit")
    public String editResources(
        @RequestParam("resourceType") String type,
        @RequestParam("title") String title,
        @RequestParam("category") String category,
        @RequestParam("description") String description,
        @RequestParam("url") String url,
        @RequestParam("contentID") String contentID,
        HttpSession session,
        Model model,
        RedirectAttributes redirectAttributes
            ) {

        if (!isProfessional(session)) return "redirect:/login";
                
        Map<String, String> errors = new HashMap<>();
                
        // Validate resourceType
        if (type == null || type.trim().isEmpty()) {
            errors.put("resourceType" , "Resource type is required");
        } else if (!type.equals("pdf") && !type.equals("video")) {
            errors.put("resourceType", "Invalid resource type");
        }
        
        // Validate title
        if (title == null || title.trim().isEmpty()) {
            errors.put("title", "Title is required");
        } else if (title.trim().length() < 3) {
            errors.put("title", "Title must be at least 3 characters long");
        }
        
        // Validate category
        if (category == null || category.trim().isEmpty()) {
            errors.put("category", "Category is required");
        }
        
        // Validate description
        if (description == null || description.trim().isEmpty()) {
            errors.put("description", "Description is required");
        } else if (description.trim().length() < 10) {
            errors.put("description", "Description must be at least 10 characters long");
        }
        
        // Validate URL
        if (url == null || url.trim().isEmpty()) {
            errors.put("url", "URL is required");
        } else if (!url.startsWith("http://") && !url.startsWith("https://")) {
            errors.put("url", "URL must start with http:// or https://");
        }
        
        // If there are any errors, return to the form
        if (!errors.isEmpty()) {
            redirectAttributes.addFlashAttribute("errors", errors);
            redirectAttributes.addFlashAttribute("title", title);
            redirectAttributes.addFlashAttribute("category", category);
            redirectAttributes.addFlashAttribute("description", description);
            redirectAttributes.addFlashAttribute("url", url);
            redirectAttributes.addFlashAttribute("resourceType", type);
            return "redirect:/professional/resources/edit?contentID=" + contentID;
        }
        
        User professional = (User) session.getAttribute("user");
        Category categoryObj = new Category();
        categoryObj.setCategoryID(Integer.parseInt(category));
        // int categoryID = contentDao.getCategoryID(category);
        SubContent updatedSubContent = new SubContent(title, categoryObj, description, url, professional, type, "pending", true, null);
        updatedSubContent.setContentID(Integer.parseInt(contentID));
        
        professionalDao.editContent(updatedSubContent);
        redirectAttributes.addFlashAttribute("success", "Resource updated successfully!");
        return "redirect:/professional/my-resources";
         
    }


    @GetMapping("/resources")
    public String showResourcesPage(HttpSession session, Model model) {
        if (!isProfessional(session)) return "redirect:/login";
        
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

   

    // Helper to secure professional routes
    private boolean isProfessional(HttpSession session) {
        String role = (String) session.getAttribute("role");
        return "PROFESSIONAL".equalsIgnoreCase(role);
    }
}