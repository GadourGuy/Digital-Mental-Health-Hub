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
@Table(name = "assessments")
public class AssessmentEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private int totalScore;
    private String severity; // e.g., "Normal", "Mild", "Severe"
    private LocalDateTime date;

    // Constructors, Getters, Setters
    public AssessmentEntry() {}
    
    // ... generate getters and setters ...
    public void setUser(User user) { this.user = user; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public int getTotalScore() { return totalScore; }
}