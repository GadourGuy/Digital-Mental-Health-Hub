package com.secj3303.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "completed_content")
public class CompletedContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @Column(name = "contentID", nullable = false)
    private int contentID;
    
    @Column(name = "contentCategoryID", nullable = false)
    private int contentCategoryID;
    
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
    
    @Column(name = "completion_date")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date completionDate;

    // Constructors
    public CompletedContent() {}
    
    public CompletedContent(int contentID, int contentCategoryID, User user) {
        this.contentID = contentID;
        this.contentCategoryID = contentCategoryID;
        this.user = user;
        this.completionDate = new java.util.Date();
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getContentID() { return contentID; }
    public void setContentID(int contentID) { this.contentID = contentID; }
    
    public int getContentCategoryID() { return contentCategoryID; }
    public void setContentCategoryID(int contentCategoryID) { 
        this.contentCategoryID = contentCategoryID; 
    }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public java.util.Date getCompletionDate() { return completionDate; }
    public void setCompletionDate(java.util.Date completionDate) { 
        this.completionDate = completionDate; 
    }
}