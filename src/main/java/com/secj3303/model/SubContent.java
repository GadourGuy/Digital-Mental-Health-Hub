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

    @Column(name = "type")
    private String type;
    
    @Column(name = "contentURL")
    private String contentURL;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "professionalID", nullable = false)
    private User professional;

    // Constructors
    public SubContent() {}
    public SubContent(String contentTitle, Category contentCategory, String description, String contentURL, User professional, String type) {
        this.contentTitle = contentTitle;
        this.contentCategory = contentCategory;
        this.description = description;
        this.contentURL = contentURL;
        this.status = false;
        this.professional = professional;
        this.type = type;
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
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getContentURL() { return contentURL; }
    public void setContentURL(String contentURL) { this.contentURL = contentURL; }

    public boolean getStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }

    public User getProfessional() { return professional; }
    public void setProfessional(User professional) { this.professional = professional; }
}