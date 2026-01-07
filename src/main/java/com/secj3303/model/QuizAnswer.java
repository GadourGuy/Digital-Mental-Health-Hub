package com.secj3303.model;

import javax.persistence.*;


@Entity
@Table(name = "quiz_answers")
public class QuizAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answerID")
    private int answerID;
    
    @ManyToOne
    @JoinColumn(name = "quizID", nullable = false)
    private QuizQuestion quizQuestion;
    
    @Column(name = "answer_text", nullable = false, columnDefinition = "TEXT")
    private String answerText;
    
    @Column(name = "is_correct")
    private boolean isCorrect;

    // Constructors
    public QuizAnswer() {}
    
    public QuizAnswer(QuizQuestion quizQuestion, String answerText, boolean isCorrect) {
        this.quizQuestion = quizQuestion;
        this.answerText = answerText;
        this.isCorrect = isCorrect;
    }

    // Getters and Setters
    public int getAnswerID() { return answerID; }
    public void setAnswerID(int answerID) { this.answerID = answerID; }
    
    public QuizQuestion getQuizQuestion() { return quizQuestion; }
    public void setQuizQuestion(QuizQuestion quizQuestion) { this.quizQuestion = quizQuestion; }
    
    public String getAnswerText() { return answerText; }
    public void setAnswerText(String answerText) { this.answerText = answerText; }
    
    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
}