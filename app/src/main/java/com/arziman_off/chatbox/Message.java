package com.arziman_off.chatbox;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message{
    private String text;
    private String senderId;
    private String receiverId;
    private String sentAt;

    public Message() {
    }

    public Message(String text, String senderId, String receiverId) {
        this.text = text;
        this.senderId = senderId;
        this.receiverId = receiverId;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = LocalDateTime.now().format(formatter);
        this.sentAt = formattedDate;
    }

    public String getText() {
        return text;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSentAt() {
        return sentAt;
    }

    @Override
    public String toString() {
        return "Message{" +
                "text='" + text + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", sentAt=" + sentAt +
                '}';
    }
}
