package com.secj3303.model;

public class ChatResponse {
    private String reply;

    public ChatResponse(String reply) { this.reply = reply; }
    
    // Getters and Setters
    public String getReply() { return reply; }
    public void setReply(String reply) { this.reply = reply; }
}