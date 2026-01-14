package com.secj3303.dao.professional;

import java.util.List;

import com.secj3303.model.ProfessionalRequest;

public interface ProfessionalDao {

    // get number of professionals in the system
    public int getAllProfessionals();

    // get the number of users request for being professional
    public int getProfessionalRequests();

    // get all pending requests
    public List<ProfessionalRequest> getAllPendingProfessionalRequests();

    public ProfessionalRequest getSingleProfessionalRequest(int requestID);

}
