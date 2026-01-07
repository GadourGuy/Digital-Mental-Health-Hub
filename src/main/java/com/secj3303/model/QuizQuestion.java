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
    private User users;
    
    @OneToMany(mappedBy = "quizQuestion", cascade = CascadeType.ALL)
    private Set<QuizAnswer> answers = new HashSet<>();

    // Constructors
    public QuizQuestion() {}
    
    public QuizQuestion(String content, User users) {
        this.content = content;
        this.users = users;
    }

    // Getters and Setters
    public int getQuizID() { return quizID; }
    public void setQuizID(int quizID) { this.quizID = quizID; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public User getUsers() { return users; }
    public void setUsers(User users) { this.users = users; }
    
    public Set<QuizAnswer> getAnswers() { return answers; }
    public void setAnswers(Set<QuizAnswer> answers) { this.answers = answers; }
}