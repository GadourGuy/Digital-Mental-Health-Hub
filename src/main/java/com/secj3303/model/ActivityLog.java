package com.secj3303.model;

import java.time.LocalDateTime;

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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String type; // "MOOD", "EXERCISE", "READING"
    private String title;
    private String status; // "COMPLETED"
    private LocalDateTime date;

    // Getters and Setters
    public String getType() { return type; }
    public String getTitle() { return title; }
    public LocalDateTime getDate() { return date; }
    // ... other getters/setters
}