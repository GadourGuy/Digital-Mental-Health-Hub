package com.secj3303.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "post_comments")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentID;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    
    @ManyToOne
    @JoinColumn(name = "userID")
    private User users;

    @ManyToOne
    @JoinColumn(name = "postID")
    private ForumPost post;

    public PostComment() {
        this.createdAt = new Date();
    }

    public PostComment(String content, User users, ForumPost post) {
        this.content = content;
        this.users = users;
        this.post = post;
        this.createdAt = new Date();
    }

    // Getters and Setters
    public int getCommentID() { return commentID; }
    public void setCommentID(int commentID) { this.commentID = commentID; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
    public User getUsers() { return users; }
    public void setUsers(User users) { this.users = users; }
    public ForumPost getPost() { return post; }
    public void setPost(ForumPost post) { this.post = post; }
}