package com.secj3303.dao.admin;

import java.util.List;

import com.secj3303.model.Feedback;
import com.secj3303.model.ForumPost;
import com.secj3303.model.MoodEntry;
import com.secj3303.model.ProfessionalRequest;
import com.secj3303.model.SubContent;
import com.secj3303.model.User;

public interface AdminDao {

    // get the user info (name, id, email) from users table based on professionalRequest id
    public List<User> getUsersRequesting(List<ProfessionalRequest> professionalRequests);


    // let the admin to eaither approve or reject 
    public void editPendingRequest(int requestID, String status, String rejectionMessage);

    // to get the content whose status is pending
    public List<SubContent> getAllPendingContent();

    // USERS

    // get user all posts in the system
    public List<ForumPost> getUserPostByID(int userID);
    

    // get the total number of completed content
    public int getCompletedResourcesCount(int userID);


    // feedback
    public List<Feedback> getAllFeedbacks();


}
