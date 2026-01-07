package com.secj3303.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

// ============ User Entity ============
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID")
    private int userID;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "role", nullable = false)
    private String role;
    
    @Column(name = "password", nullable = false)
    private String password;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<ForumPost> forumPosts = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<PostComment> comments = new HashSet<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<CompletedContent> completedContents = new HashSet<>();

    // Constructors
    public User() {}
    
    public User(String name, String email, String role, String password) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    // Getters and Setters
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public Set<ForumPost> getForumPosts() { return forumPosts; }
    public void setForumPosts(Set<ForumPost> forumPosts) { this.forumPosts = forumPosts; }
    
    public Set<PostComment> getComments() { return comments; }
    public void setComments(Set<PostComment> comments) { this.comments = comments; }
    
    public Set<CompletedContent> getCompletedContents() { return completedContents; }
    public void setCompletedContents(Set<CompletedContent> completedContents) { 
        this.completedContents = completedContents; 
    }
}
