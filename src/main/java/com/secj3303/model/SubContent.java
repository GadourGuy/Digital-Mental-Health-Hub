package com.secj3303.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "sub_content")
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
    private String status;

    public String getRejectionReason() {
        return rejectionReason;
    }
    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
    public boolean isEdited() {
        return isEdited;
    }
    public void setEdited(boolean isEdited) {
        this.isEdited = isEdited;
    }
    @Column(name = "rejection_reason")
    private String rejectionReason;

    @Column(name = "isEditied")
    private boolean isEdited;

    @ManyToOne
    @JoinColumn(name = "professionalID", nullable = false)
    private User professional;

    // Constructors
    public SubContent() {}
    
    // to send the data with default values to the db
    public SubContent(String contentTitle, Category contentCategory, String description, String contentURL, User professional, String type) {
        this.contentTitle = contentTitle;
        this.contentCategory = contentCategory;
        this.description = description;
        this.contentURL = contentURL;
        this.status = "pending";
        this.professional = professional;
        this.type = type;
        this.isEdited = false;
        this.rejectionReason = null;
    }

    // to retireve all data from db
    public SubContent(String contentTitle, Category contentCategory, String description, String contentURL, User professional, String type, String status, boolean isEdited, String rejectionReason) {
        this.contentTitle = contentTitle;
        this.contentCategory = contentCategory;
        this.description = description;
        this.contentURL = contentURL;
        this.status = status;
        this.professional = professional;
        this.type = type;
        this.isEdited = isEdited;
        this.rejectionReason = rejectionReason;
    }

    public String getContentDescription() {
        return description;
    }
    public Category getContentCategory() {
        return contentCategory;
    }

    public void setContentCategory(Category contentCategory) {
        this.contentCategory = contentCategory;
    }
    // Other Getters and Setters
    public int getContentID() { return contentID; }
    public void setContentID(int contentID) { this.contentID = contentID; }

    public String getContentTitle() { return contentTitle; }
    public void setContentTitle(String contentTitle) { this.contentTitle = contentTitle; }
    
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getContentURL() { return contentURL; }
    public void setContentURL(String contentURL) { this.contentURL = contentURL; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getProfessional() { return professional; }
    public void setProfessional(User professional) { this.professional = professional; }
}