package com.secj3303.dao.admin;

import java.util.List;

import com.secj3303.model.User;

public interface AdminDao {
    // get users from db for the admin, excluding the admins user info
    public List<User> getAllUsers();

    // retrieve user info by id
    public User getUser(int id);

    // approve the professional request, based on professional id
    public void updateProfessionalStatus(int id);

    // approve professional's content based on content id
    public void changeProfessionalContentStatus(int id);

}
