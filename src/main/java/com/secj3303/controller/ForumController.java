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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
            // Inside showForum method...

// A. Fetch All Posts - Filter out posts where user is null!
Query<ForumPost> qAll = hibernateSession.createQuery(
    "FROM ForumPost p WHERE p.users IS NOT NULL ORDER BY p.createdAt DESC", 
    ForumPost.class
);
	List<ForumPost> allPosts = qAll.list();

	// B. Fetch My Posts
	Query<ForumPost> qMy = hibernateSession.createQuery(
    	"FROM ForumPost p WHERE p.users.userID = :uid ORDER BY p.createdAt DESC", 
    	ForumPost.class
	);
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

    /**
     * Helper method to handle Deep Lazy Initialization.
     * Prevents LazyInitializationException when Thymeleaf tries to access:
     * post.comments.users.name
     */
    private void initializePosts(List<ForumPost> posts) {
        for (ForumPost p : posts) {
            Hibernate.initialize(p.getUsers()); // The post author
            
            Hibernate.initialize(p.getLikes()); // The list of likes
            // Load the USER who liked the post
            p.getLikes().forEach(like -> Hibernate.initialize(like.getUsers())); 

            Hibernate.initialize(p.getComments()); // The list of comments
            // Load the USER who wrote the comment
            p.getComments().forEach(comment -> Hibernate.initialize(comment.getUsers())); 
        }
    }

    // 2. CREATE POST
    @PostMapping("/create")
    public String createPost(@RequestParam("content") String content, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();

            // --- FIX: Re-attach User ---
            // The 'currentUser' from session is "detached". We must load it into the current Hibernate session.
            User dbUser = hibernateSession.load(User.class, currentUser.getUserID());
            
            ForumPost newPost = new ForumPost();
            newPost.setContent(content);
            newPost.setUsers(dbUser); // Use the attached user
            
            hibernateSession.save(newPost);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/forum";
    }

    // 3. HANDLE LIKES (Toggle Logic)
    @PostMapping("/like/{postId}")
    public String likePost(@PathVariable("postId") int postId, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            
            // Re-attach user
            User dbUser = hibernateSession.load(User.class, currentUser.getUserID());
            ForumPost post = hibernateSession.get(ForumPost.class, postId);
            
            Query<PostLike> query = hibernateSession.createQuery(
                "FROM PostLike WHERE post.postID = :pid AND users.userID = :uid", PostLike.class);
            query.setParameter("pid", postId);
            query.setParameter("uid", currentUser.getUserID());
            PostLike existingLike = query.uniqueResult();

            if (existingLike != null) {
                // Already liked -> Unlike (Delete)
                hibernateSession.delete(existingLike);
            } else {
                // Not liked -> Like (Insert)
                PostLike newLike = new PostLike(dbUser, post); // Use attached dbUser
                hibernateSession.save(newLike);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Redirect back to the same page to refresh data
        return "redirect:/forum";
    }

    // 4. ADD COMMENT
    @PostMapping("/comment/{postId}")
    public String addComment(@PathVariable("postId") int postId, @RequestParam("content") String content, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            
            // Re-attach user
            User dbUser = hibernateSession.load(User.class, currentUser.getUserID());
            ForumPost post = hibernateSession.get(ForumPost.class, postId);
            
            PostComment comment = new PostComment(content, dbUser, post); // Use attached dbUser
            hibernateSession.save(comment);
            
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/forum";
    }

    // 5. UPDATE POST
    @PostMapping("/update")
    public String updatePost(@ModelAttribute ForumPost updatedPost, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";

        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            
            ForumPost existingPost = hibernateSession.get(ForumPost.class, updatedPost.getPostID());

            if (existingPost != null) {
                // Permission Check: Author
                
                boolean isAuthor = currentUser.getUserID() == existingPost.getUsers().getUserID();

                if (isAuthor) {
                    existingPost.setContent(updatedPost.getContent());
                    hibernateSession.update(existingPost);
                    tx.commit();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/forum"; 
    }

    // 6. DELETE POST
    @GetMapping("/delete/{id}")
    public String deletePost(@PathVariable("id") int id, HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) return "redirect:/login";
        
        try (Session hibernateSession = sessionFactory.openSession()) {
            Transaction tx = hibernateSession.beginTransaction();
            ForumPost post = hibernateSession.get(ForumPost.class, id);

            if (post != null) {
                // 1. Re-fetch current user from DB to ensure role is accurate
                User dbUser = hibernateSession.get(User.class, currentUser.getUserID());
                String currentRole = dbUser.getRole();
                
                // Debugging: Print to console to see what the role actually is
                System.out.println("User ID: " + dbUser.getUserID() + " | Role: " + currentRole);

                // 2. Robust Permission Check
                // Check if role contains "ADMIN" (case insensitive) to handle "Admin", "ADMIN ", etc.
                boolean isAdmin = currentRole != null && currentRole.trim().equalsIgnoreCase("ADMIN");
                
                // Check if user is the author
                // (Use Safe Navigation in case post.getUsers() is null)
                boolean isAuthor = post.getUsers() != null && dbUser.getUserID() == post.getUsers().getUserID();

                if (isAdmin || isAuthor) {
                    hibernateSession.delete(post);
                    tx.commit();
                    System.out.println("Post deleted successfully.");
                } else {
                    System.out.println("Delete failed: User is neither Admin nor Author.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "redirect:/forum";
    }
}