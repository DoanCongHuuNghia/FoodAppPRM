package com.code.foodapp.models;

public class Message {
    private String sender;
    private String message;
    private String time;

    public Message(String sender, String message, String time) {
        this.sender = sender;
        this.message = message;
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return time;
    }
}
