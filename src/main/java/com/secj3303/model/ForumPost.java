package com.secj3303.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "forum_post")
public class ForumPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "postID")
    private int postID;
    
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
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
    
    public ForumPost(User user, String content) {
        this.user = user;
        this.content = content;
        this.createdAt = new java.util.Date();
    }

    // Getters and Setters
    public int getPostID() { return postID; }
    public void setPostID(int postID) { this.postID = postID; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public java.util.Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.util.Date createdAt) { this.createdAt = createdAt; }
    
    public Set<PostComment> getComments() { return comments; }
    public void setComments(Set<PostComment> comments) { this.comments = comments; }
    
    public Set<PostLike> getLikes() { return likes; }
    public void setLikes(Set<PostLike> likes) { this.likes = likes; }
}