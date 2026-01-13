package com.secj3303.dao.content;

import java.util.List;

import com.secj3303.model.CompletedContent;

public interface CompletedContentDao {
    void saveCompletedContent(CompletedContent entry);
    boolean hasUserCompleted(int userId, int contentId);
	List<Integer> getCompletedContentIds(int userId);
}