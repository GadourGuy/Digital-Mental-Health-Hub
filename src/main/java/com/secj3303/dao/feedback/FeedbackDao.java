package com.secj3303.dao.feedback;

import java.util.List;

import com.secj3303.model.Feedback;

public interface FeedbackDao {
    void saveFeedback(Feedback feedback);
    List<Feedback> getAllFeedbacks();
    public List<Feedback> getUserFeedback(int userID);
}