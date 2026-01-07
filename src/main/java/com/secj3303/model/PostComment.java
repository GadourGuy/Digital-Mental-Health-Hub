package com.secj3303.model;

import javax.persistence.*;


@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentID")
    private int commentID;
    
    @ManyToOne
    @JoinColumn(name = "postID", nullable = false)
    private ForumPost post;
    
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
    
    @Column(name = "comment", nullable = false, columnDefinition = "TEXT")
    private String comment;
    
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date createdAt;

    // Constructors
    public PostComment() {
        this.createdAt = new java.util.Date();
    }
    
    public PostComment(ForumPost post, User user, String comment) {
        this.post = post;
        this.user = user;
        this.comment = comment;
        this.createdAt = new java.util.Date();
    }

    // Getters and Setters
    public int getCommentID() { return commentID; }
    public void setCommentID(int commentID) { this.commentID = commentID; }
    
    public ForumPost getPost() { return post; }
    public void setPost(ForumPost post) { this.post = post; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public java.util.Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(java.util.Date createdAt) { this.createdAt = createdAt; }
}