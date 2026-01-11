package com.secj3303.model;

import javax.persistence.*;

@Entity
@Table(name = "professional_requests")
public class ProfessionalRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "requestID")
    private int requestID;

    // One User can have One Request (Enforced by unique constraint in logic)
    @OneToOne
    @JoinColumn(name = "userID", nullable = false, unique = true)
    private User user;

    @Column(name = "cv_link", nullable = false)
    private String cvLink;

    // Status: "pending", "approved", "rejected"
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "rejection_reason")
    private String rejectionReason;

    // --- Constructors ---
    public ProfessionalRequest() {}

    public ProfessionalRequest(User user, String cvLink) {
        this.user = user;
        this.cvLink = cvLink;
        this.status = "pending"; // Default status
        this.rejectionReason = null;
    }

    // --- Getters and Setters ---
    public int getRequestID() { return requestID; }
    public void setRequestID(int requestID) { this.requestID = requestID; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCvLink() { return cvLink; }
    public void setCvLink(String cvLink) { this.cvLink = cvLink; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRejectionReason() { return rejectionReason; }
    public void setRejectionReason(String rejectionReason) { this.rejectionReason = rejectionReason; }
}