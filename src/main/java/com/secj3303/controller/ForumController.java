package com.secj3303.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.secj3303.model.SubContent;
import com.secj3303.model.User;

@Controller
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private SessionFactory sessionFactory;

    // 1. DISPLAY FORUM (Feed)
    @GetMapping
    public String showForum(Model model, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try (Session hibernateSession = sessionFactory.openSession()) {
            // Order by ID desc so newest posts appear first
            Query<SubContent> query = hibernateSession.createQuery("FROM SubContent ORDER BY contentID DESC", SubContent.class);
            List<SubContent> posts = query.list();
            model.addAttribute("posts", posts);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return "forum_page"; 
    }

    // 2. CREATE POST (New Method)
    @PostMapping("/create")
    public String createPost(@ModelAttribute SubContent newPost, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();

            // Set default values
            newPost.setProfessional(currentUser); // Set the author
            newPost.setStatus(true); // Active by default
            
            hibernateSession.save(newPost);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/forum";
    }

    // 3. UPDATE POST
    @PostMapping("/update")
    public String updatePost(@ModelAttribute SubContent updatedPost, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            SubContent existingPost = hibernateSession.get(SubContent.class, updatedPost.getContentID());

            if (existingPost != null) {
                // Permission Check: Admin OR Author
                boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());
                boolean isAuthor = currentUser.getUserID() == existingPost.getProfessional().getUserID();

                if (isAdmin || isAuthor) {
                    existingPost.setContentTitle(updatedPost.getContentTitle());
                    existingPost.setContentDescription(updatedPost.getContentDescription());
                    hibernateSession.update(existingPost);
                    tx.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/forum"; 
    }

    // 4. DELETE POST
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        
        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            SubContent post = hibernateSession.get(SubContent.class, id);

            if (currentUser != null && post != null) {
                boolean isAdmin = "ADMIN".equalsIgnoreCase(currentUser.getRole());
                boolean isAuthor = currentUser.getUserID() == post.getProfessional().getUserID();

                if (isAdmin || isAuthor) {
                    hibernateSession.delete(post);
                    tx.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/forum";
    }
}