package com.secj3303.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.secj3303.model.ChatRequest;
import com.secj3303.model.ChatResponse;

@Controller
public class ChatController {

    // --- PASTE YOUR GROQ KEY HERE DIRECTLY ---
    // (Get this from https://console.groq.com)
    private static final String API_KEY = ""; 
    
    // Groq API URL
    private static final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";

    @GetMapping("/student/chatbot")
    public String showChatPage(HttpSession session) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "Student-Chatbot";
    }

    @PostMapping("/api/chat")
    @ResponseBody
    public ChatResponse chat(@RequestBody ChatRequest request) {
        System.out.println("DEBUG: Chat request received! Message: " + request.getMessage());

        String userMessage = request.getMessage();
        String aiReply = callGroqAI(userMessage);
        
        System.out.println("DEBUG: AI Reply: " + aiReply);

        return new ChatResponse(aiReply);
    }

    private String callGroqAI(String userMessage) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + API_KEY);


            Map<String, String> systemMsg = new HashMap<>();
            systemMsg.put("role", "system");
            systemMsg.put("content", 
                "You are a dedicated Mental Health Support Assistant. " +
                "Your ONLY scope is mental health, emotional support, and wellness. " +
                "STRICT RULES: " +
                "1. If a user asks about anything NOT related to mental health (e.g., math, coding, general facts, history, or sports), " +
                "politely refuse and state that you are only designed to discuss mental health and emotional well-being. " +
                "2. Be empathetic, kind, and brief. " +
                "3. If a user mentions self-harm, prioritize suggesting professional help immediately.");

            // User Message
            Map<String, String> userMsg = new HashMap<>();
            userMsg.put("role", "user");
            userMsg.put("content", userMessage);

            // Request Body
            Map<String, Object> body = new HashMap<>();
            body.put("model", "llama-3.3-70b-versatile"); 
            body.put("messages", List.of(systemMsg, userMsg));

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

            // Send Request
            Map<String, Object> response = restTemplate.postForObject(GROQ_URL, entity, Map.class);

            // Parse Response
            if (response != null && response.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> messageObj = (Map<String, Object>) choices.get(0).get("message");
                    return (String) messageObj.get("content");
                }
            }
            return "I am having trouble thinking right now.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error connecting to AI. Please check your connection.";
        }
    }
}