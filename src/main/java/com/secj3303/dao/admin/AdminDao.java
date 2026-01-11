package com.secj3303.dao.admin;

import java.util.List;

import com.secj3303.model.User;

public interface AdminDao {
    // get users from db for the admin, excluding the admins user info
    public List<User> getAllUsers();

    // approve the professional request, based on professional id
    public void updateProfessionalStatus(int id);

    // approve professional's content based on content id
    public void changeProfessionalContentStatus(int id);

    // get number of professionals in the system
    public int getAllProfessionals();

    // get the number of users request for being professional
    public int getProfessionalRequests();

}
