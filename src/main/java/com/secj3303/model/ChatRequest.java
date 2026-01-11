package com.secj3303.model;

public class ChatRequest {
    private String message;
    
    // 1. Default/No-Arg Constructor (REQUIRED for Spring to load JSON)
    public ChatRequest() {}

    // 2. Parameterized Constructor (Optional, helpful for testing)
    public ChatRequest(String message) {
        this.message = message;
    }

    // Getters and Setters
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}