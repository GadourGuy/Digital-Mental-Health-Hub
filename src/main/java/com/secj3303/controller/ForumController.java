package com.secj3303.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.secj3303.dao.content.ContentDao;
import com.secj3303.dao.forum.ForumPostDao;
import com.secj3303.model.Category;
import com.secj3303.model.ForumPost;
import com.secj3303.model.User;

@Controller
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumPostDao forumDao;

    @Autowired
    private ContentDao contentDao;

    // 1. DISPLAY FORUM
    @GetMapping
    public String showForum(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        List<ForumPost> allPosts = forumDao.getAllPosts();
        List<ForumPost> myPosts = forumDao.getPostsByUserId(currentUser.getUserID());
        

        List<Category> categories = contentDao.getContentCategories();
        
        model.addAttribute("categories", categories);
        model.addAttribute("allPosts", allPosts);
        model.addAttribute("myPosts", myPosts);
        model.addAttribute("currentUser", currentUser);
        
        return "forum_page"; 
    }

    // 2. CREATE POST
    @PostMapping("/create")
    public String createPost(@RequestParam("content") String content, 
                             @RequestParam("category") String category, 
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        ForumPost newPost = new ForumPost();
        newPost.setContent(content);
        newPost.setCategory(category);
        
        forumDao.createPost(newPost, currentUser.getUserID());
        
        redirectAttributes.addFlashAttribute("successMessage", "Post created successfully!");
        return "redirect:/forum";
    }

    // 3. HANDLE LIKES
    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable("postId") int postId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";
        
        forumDao.toggleLike(postId, currentUser.getUserID());
        
        return "redirect:/forum";
    }

    // 4. ADD COMMENT
    @PostMapping("/comment/{postId}")
    public String addComment(@PathVariable("postId") int postId, 
                             @RequestParam("content") String content, 
                             HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";
        
        forumDao.addComment(postId, currentUser.getUserID(), content);
        
        return "redirect:/forum";
    }

    // 5. UPDATE POST
    @PostMapping("/update")
    public String updatePost(@ModelAttribute ForumPost updatedPost, 
                             HttpSession session, 
                             RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        boolean success = forumDao.updatePost(
            updatedPost.getPostID(), 
            currentUser.getUserID(), 
            updatedPost.getContent(), 
            updatedPost.getCategory() 
        );

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Post updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Unable to update post.");
        }
        return "redirect:/forum"; 
    }

    // 6. DELETE POST
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int id, 
                             HttpSession session, 
                             RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";
        
        boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());
        boolean success = forumDao.deletePost(id, currentUser.getUserID(), isAdmin);

        if (success) {
            redirectAttributes.addFlashAttribute("successMessage", "Post deleted successfully.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete this post.");
        }
        return "redirect:/forum";
    }
}