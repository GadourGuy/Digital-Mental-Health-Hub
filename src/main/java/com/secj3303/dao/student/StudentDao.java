package com.secj3303.dao.student;

import com.secj3303.model.ProfessionalRequest;

public interface StudentDao {

    public boolean checkUserRequestExists(int userID);
    
    public void addProfessionalRequest(ProfessionalRequest professionalRequest);
}
