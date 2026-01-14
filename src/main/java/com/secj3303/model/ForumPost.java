package com.secj3303.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "forum_post")
public class ForumPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postID")
    private int postID;
    
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User users;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    
    @Column(name = "category")
    private String category; // e.g., "General", "Support", "Success Story"
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<PostComment> comments = new HashSet<>();
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<PostLike> likes = new HashSet<>();

    // Constructors
    public ForumPost() {
        this.createdAt = new java.util.Date();
    }
    
    public ForumPost(User users, String content, String category) {
        this.users = users;
        this.content = content;
        this.category = category;
        this.createdAt = new java.util.Date();
    }

    // Getters and Setters
    public int getPostID() { return postID; }
    public void setPostID(int postID) { this.postID = postID; }
    
    public User getUsers() { return users; }
    public void setUsers(User users) { this.users = users; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public java.util.Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.util.Date createdAt) { this.createdAt = createdAt; }
    
    public Set<PostComment> getComments() { return comments; }
    public void setComments(Set<PostComment> comments) { this.comments = comments; }
    
    public Set<PostLike> getLikes() { return likes; }
    public void setLikes(Set<PostLike> likes) { this.likes = likes; }
}