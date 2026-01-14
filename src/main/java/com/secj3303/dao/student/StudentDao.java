package com.secj3303.dao.student;

import java.util.List;

import com.secj3303.model.ProfessionalRequest;
import com.secj3303.model.User;

public interface StudentDao {

    public boolean checkUserRequestExists(int userID);
    
    public void addProfessionalRequest(ProfessionalRequest professionalRequest);

    // to return a num  of students
    public int getNumOfStudents();

    // get all users in the system information
    public List<User> getAllStudents();
}
