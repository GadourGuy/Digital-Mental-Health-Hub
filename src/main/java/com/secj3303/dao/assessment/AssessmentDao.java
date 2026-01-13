package com.secj3303.dao.assessment;

import java.util.List;

import com.secj3303.model.AssessmentEntry;

public interface AssessmentDao {
    // Save a new assessment result
    void saveAssessment(AssessmentEntry assessment);

    // Get the history of assessments for a specific student
    List<AssessmentEntry> getAssessmentHistory(int userId);
    
    // Get the most recent assessment (useful for the dashboard)
    AssessmentEntry getLastAssessment(int userId);
}