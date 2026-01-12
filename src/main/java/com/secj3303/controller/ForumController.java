package com.secj3303.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute; // Import this
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.secj3303.model.ForumPost;
import com.secj3303.model.PostComment;
import com.secj3303.model.PostLike;
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
            // A. Fetch All Posts
            Query<ForumPost> qAll = hibernateSession.createQuery(
                "FROM ForumPost p WHERE p.users IS NOT NULL ORDER BY p.createdAt DESC", ForumPost.class);
            List<ForumPost> allPosts = qAll.list();

            // B. Fetch My Posts
            Query<ForumPost> qMy = hibernateSession.createQuery(
                "FROM ForumPost p WHERE p.users.userID = :uid ORDER BY p.createdAt DESC", ForumPost.class);
            qMy.setParameter("uid", currentUser.getUserID());
            List<ForumPost> myPosts = qMy.list();

            initializePosts(allPosts);
            initializePosts(myPosts); 

            model.addAttribute("allPosts", allPosts);
            model.addAttribute("myPosts", myPosts);
            model.addAttribute("currentUser", currentUser);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "forum_page"; 
    }

    private void initializePosts(List<ForumPost> posts) {
        for (ForumPost p : posts) {
            Hibernate.initialize(p.getUsers());
            Hibernate.initialize(p.getLikes());
            p.getLikes().forEach(like -> Hibernate.initialize(like.getUsers())); 
            Hibernate.initialize(p.getComments());
            p.getComments().forEach(comment -> Hibernate.initialize(comment.getUsers())); 
        }
    }

    // 2. CREATE POST (Added Category param)
    @PostMapping("/create")
    public String createPost(@RequestParam("content") String content, 
                             @RequestParam("category") String category, 
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            User dbUser = hibernateSession.load(User.class, currentUser.getUserID());
            
            ForumPost newPost = new ForumPost();
            newPost.setContent(content);
            newPost.setCategory(category); // Set Category
            newPost.setUsers(dbUser);
            
            hibernateSession.save(newPost);
            tx.commit();
            redirectAttributes.addFlashAttribute("successMessage", "Post created successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/forum";
    }

    // 3. HANDLE LIKES
    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable("postId") int postId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";
        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            User dbUser = hibernateSession.load(User.class, currentUser.getUserID());
            ForumPost post = hibernateSession.get(ForumPost.class, postId);
            Query<PostLike> query = hibernateSession.createQuery(
                "FROM PostLike WHERE post.postID = :pid AND users.userID = :uid", PostLike.class);
            query.setParameter("pid", postId);
            query.setParameter("uid", currentUser.getUserID());
            PostLike existingLike = query.uniqueResult();
            if (existingLike != null) {
                hibernateSession.delete(existingLike);
            } else {
                PostLike newLike = new PostLike(dbUser, post);
                hibernateSession.save(newLike);
            }
            tx.commit();
        } catch (Exception e) { e.printStackTrace(); }
        return "redirect:/forum";
    }

    // 4. ADD COMMENT
    @PostMapping("/comment/{postId}")
    public String addComment(@PathVariable("postId") int postId, @RequestParam("content") String content, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";
        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            User dbUser = hibernateSession.load(User.class, currentUser.getUserID());
            ForumPost post = hibernateSession.get(ForumPost.class, postId);
            PostComment comment = new PostComment(content, dbUser, post);
            hibernateSession.save(comment);
            tx.commit();
        } catch (Exception e) { e.printStackTrace(); }
        return "redirect:/forum";
    }

    // 5. UPDATE POST (Added RedirectAttributes)
    @PostMapping("/update")
    public String updatePost(@ModelAttribute ForumPost updatedPost, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            ForumPost existingPost = hibernateSession.get(ForumPost.class, updatedPost.getPostID());

            if (existingPost != null) {
                boolean isAuthor = currentUser.getUserID() == existingPost.getUsers().getUserID();
                if (isAuthor) {
                    existingPost.setContent(updatedPost.getContent());
                    // Optionally update category too if you add it to the edit form
                    hibernateSession.update(existingPost);
                    tx.commit();
                    redirectAttributes.addFlashAttribute("successMessage", "Post updated successfully!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/forum"; 
    }

    // 6. DELETE POST (Added RedirectAttributes)
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int id, HttpSession session, RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";
        
        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            ForumPost post = hibernateSession.get(ForumPost.class, id);

            if (post != null) {
                User dbUser = hibernateSession.get(User.class, currentUser.getUserID());
                String currentRole = dbUser.getRole();
                
                boolean isAdmin = currentRole != null && currentRole.trim().equalsIgnoreCase("ADMIN");
                boolean isAuthor = post.getUsers() != null && dbUser.getUserID() == post.getUsers().getUserID();

                if (isAdmin || isAuthor) {
                    hibernateSession.delete(post);
                    tx.commit();
                    redirectAttributes.addFlashAttribute("successMessage", "Post deleted successfully.");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", "You are not authorized to delete this post.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/forum";
    }
}