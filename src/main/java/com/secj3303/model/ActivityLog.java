package com.secj3303.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "activity_logs")
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "type")
    private String type; // e.g., "MOOD", "EXERCISE", "READING"

    @Column(name = "title")
    private String title; // e.g., "Gratitude Journaling"

    @Column(name = "status")
    private String status; // "COMPLETED"

    @Column(name = "date")
    private LocalDateTime date;

    // --- Constructors ---
    public ActivityLog() {
        this.date = LocalDateTime.now(); // Default timestamp
    }

    public ActivityLog(User user, String type, String title, String status) {
        this.user = user;
        this.type = type;
        this.title = title;
        this.status = status;
        this.date = LocalDateTime.now();
    }

    // --- Getters and Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}