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
@Table(name = "mood_entries")
public class MoodEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String mood; // e.g., "Excellent", "Bad"
    private int score;   // 5 = Excellent, 1 = Bad (For the Graph)
    private String note; // The optional text note
    private LocalDateTime date;

    // Constructors
    public MoodEntry() {}
    public MoodEntry(User user, String mood, int score, String note, LocalDateTime date) {
        this.user = user;
        this.mood = mood;
        this.score = score;
        this.note = note;
        this.date = date;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}