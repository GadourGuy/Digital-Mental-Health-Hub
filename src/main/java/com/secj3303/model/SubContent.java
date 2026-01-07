package com.secj3303.model;

import javax.persistence.*;

@Entity
@Table(name = "sub_contents")
public class SubContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contentID")
    private int contentID;
    
    @Column(name = "content_title", nullable = false)
    private String contentTitle;
    
    @ManyToOne
    @JoinColumn(name = "contentCategoryID", nullable = false)
    private Category contentCategory;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "contentURL")
    private String contentURL;

    // Constructors
    public SubContent() {}
    
    public SubContent(String contentTitle, Category contentCategory, String description, String contentURL) {
        this.contentTitle = contentTitle;
        this.contentCategory = contentCategory;
        this.description = description;
        this.contentURL = contentURL;
    }

    // Getters and Setters
    public int getContentID() { return contentID; }
    public void setContentID(int contentID) { this.contentID = contentID; }
    
    public String getContentTitle() { return contentTitle; }
    public void setContentTitle(String contentTitle) { this.contentTitle = contentTitle; }
    
    public Category getContentCategory() { return contentCategory; }
    public void setContentCategory(Category contentCategory) { 
        this.contentCategory = contentCategory; 
    }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getContentURL() { return contentURL; }
    public void setContentURL(String contentURL) { this.contentURL = contentURL; }
}