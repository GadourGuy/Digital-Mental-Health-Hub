package com.secj3303.dao.content;

import java.util.List;

import com.secj3303.model.CompletedContent;

public interface CompletedContentDao {
    void saveCompletedContent(CompletedContent entry);
    boolean hasUserCompleted(int userId, int contentId);
	List<Integer> getCompletedContentIds(int userId);

    // returns the number of content completions based on the professional id provided
    // it returns the total nuumber of completed content uploaded by the professional in the id
    public int GetProfessionalCompletedContent(int professionalID);
}