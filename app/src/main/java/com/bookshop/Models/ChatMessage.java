package com.bookshop.Models;

public class ChatMessage {

    private String message;
    private String sender;

    public ChatMessage() {
        // Empty constructor needed for Firestore
    }

    public ChatMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
