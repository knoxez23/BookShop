package com.bookshop.Models;

import java.util.Date;

public class ChatMessage {

    private String message;
    private String sender;
    private Date timestamp;
    private boolean dateTag;

    public ChatMessage() {
        // Empty constructor needed for Firestore
    }

    public ChatMessage(String message, String sender, Date timestamp, boolean dateTag) {
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
        this.dateTag = dateTag;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isDateTag() {
        return dateTag;
    }

    public void setDateTag(boolean dateTag) {
        this.dateTag = dateTag;
    }
}
