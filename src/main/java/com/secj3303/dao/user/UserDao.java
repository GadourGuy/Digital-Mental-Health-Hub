package com.secj3303.dao.user;

import java.util.List;

import com.secj3303.model.User;

// this interface is used by all users to maintain the user information, for the get info, update and delete user
public interface UserDao {

    // get the current user info
    public User getUser(int id);

    // udpate the current user info
    public void updateUser(int id);

    // delete the user (if the user decided to delete the account)
    // the admin can delete the user info as well
    public void deleteUser(int id);

    // when signing up, insert the user to the database
    public void insertUser(User user);



    // get user by email
    public User getUserByEmail(String email);

    // get the total number of users
    public int getTotalUsers();
    // find the users by role
     List<User> findUsersByRole(String role);

}
