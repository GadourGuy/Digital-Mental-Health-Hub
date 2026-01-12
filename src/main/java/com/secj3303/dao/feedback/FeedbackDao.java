package com.secj3303.dao.feedback;

import java.util.List;

import com.secj3303.model.Feedback;

public interface FeedbackDao {
    void saveFeedback(Feedback feedback);
    List<Feedback> getAllFeedbacks();
    // You can add more methods here later (e.g., deleteFeedback, findById)
}