package com.secj3303.model;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "quiz_questions")
public class QuizQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quizID")
    private int quizID;
    
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;
    
    @ManyToOne
    @JoinColumn(name = "userID", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "quizQuestion", cascade = CascadeType.ALL)
    private Set<QuizAnswer> answers = new HashSet<>();

    // Constructors
    public QuizQuestion() {}
    
    public QuizQuestion(String content, User user) {
        this.content = content;
        this.user = user;
    }

    // Getters and Setters
    public int getQuizID() { return quizID; }
    public void setQuizID(int quizID) { this.quizID = quizID; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    
    public Set<QuizAnswer> getAnswers() { return answers; }
    public void setAnswers(Set<QuizAnswer> answers) { this.answers = answers; }
}