package com.secj3303.dao.admin;

import java.util.List;

import com.secj3303.model.ForumPost;
import com.secj3303.model.MoodEntry;
import com.secj3303.model.ProfessionalRequest;
import com.secj3303.model.SubContent;
import com.secj3303.model.User;

public interface AdminDao {
    
    // approve professional's content based on content id
    public void changeProfessionalContentStatus(int id, String status, String message);

    // get number of professionals in the system
    public int getAllProfessionals();

    // get the number of users request for being professional
    public int getProfessionalRequests();

    // get all pending requests
    public List<ProfessionalRequest> getAllPendingProfessionalRequests();
    // get the user info (name, id, email) from users table based on professionalRequest id
    public List<User> getUsersRequesting(List<ProfessionalRequest> professionalRequests);

    public ProfessionalRequest getSingleProfessionalRequest(int requestID);

    // let the admin to eaither approve or reject 
    public void editPendingRequest(int requestID, String status, String rejectionMessage);

    // to get the content whose status is pending
    public List<SubContent> getAllPendingContent();

    // USERS
    // get all users in the system information
    public List<User> getAllStudents();

    // get user all posts in the system
    public List<ForumPost> getUserPostByID(int userID);
    

    // get the total number of completed content
    public int getCompletedResourcesCount(int userID);

    // get all users latest mood entry
    public List<MoodEntry> getUsersMood();

    // get all mood entries of a user by id
    public List<MoodEntry> getUserMoodsByID(int studentID);

}
