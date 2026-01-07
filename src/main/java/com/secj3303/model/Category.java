package com.secj3303.model;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "content_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryID")
    private int categoryID;
    
    @Column(name = "content_title", nullable = false)
    private String contentTitle;
    
    @OneToMany(mappedBy = "contentCategory", cascade = CascadeType.ALL)
    private Set<SubContent> subContents = new HashSet<>();

    // Constructors
    public Category() {}
    
    public Category(String contentTitle) {
        this.contentTitle = contentTitle;
    }

    // Getters and Setters
    public int getCategoryID() { return categoryID; }
    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }
    
    public String getContentTitle() { return contentTitle; }
    public void setContentTitle(String contentTitle) { this.contentTitle = contentTitle; }
    
    public Set<SubContent> getSubContents() { return subContents; }
    public void setSubContents(Set<SubContent> subContents) { this.subContents = subContents; }
}