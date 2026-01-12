package com.secj3303.dao.activity;

import java.util.List;

import com.secj3303.model.ActivityLog;

public interface ActivityDao {
    long getWeeklyCompletedCount(int userId);
    List<ActivityLog> getRecentActivities(int userId);
    List<ActivityLog> getWeeklyLogs(int userId); // Added this method
}