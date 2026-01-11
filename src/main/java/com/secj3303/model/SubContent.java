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

    @Column(name = "content_title")
    private String contentTitle;

    // --- THIS IS THE MISSING FIELD ---
    @Column(name = "content_description", columnDefinition = "TEXT")
    private String contentDescription;

    @ManyToOne
    @JoinColumn(name = "professionalID")
    private User professional;

    @Column(name = "status")
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "category_id") // Use your actual database foreign key column name here
    private Category contentCategory; // <--- The name MUST be "contentCategory" to match your error

    // --- GETTERS AND SETTERS ---

    // This is the specific method your error says is missing
    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }

    public String getContentDescription() {
        return contentDescription;
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

    public User getProfessional() { return professional; }
    public void setProfessional(User professional) { this.professional = professional; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}