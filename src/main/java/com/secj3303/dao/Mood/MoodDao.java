package com.secj3303.dao.Mood;

import java.util.List;

import com.secj3303.model.MoodEntry;

public interface MoodDao {
    long getWeeklyMoodCount(int userId);
    public void saveMood(MoodEntry entry);
    public List<MoodEntry> getRecentMoods(int userId);
    
    // get all users latest mood entry
    public List<MoodEntry> getUsersMood();
    
}