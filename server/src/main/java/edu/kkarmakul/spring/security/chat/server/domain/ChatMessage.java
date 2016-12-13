package edu.kkarmakul.spring.security.chat.server.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = ChatMessage.TABLE_NAME)
public class ChatMessage {
    public static final String TABLE_NAME = "chat_message";
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String message;
    private LocalDateTime timeStamp;

    public ChatMessage(String message) {
        this.message = message;
        timeStamp = LocalDateTime.now();
    }

    // For JPA
    ChatMessage() {
    }

    public String getMessage() {
        return message;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getTimestamp() {
        return timeStamp;
    }
}
