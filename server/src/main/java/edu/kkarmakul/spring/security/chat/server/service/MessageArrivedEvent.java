package edu.kkarmakul.spring.security.chat.server.service;

import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class MessageArrivedEvent extends ApplicationEvent {
    private final LocalDateTime timeStamp;
    private final String message;

    public MessageArrivedEvent(Object source, LocalDateTime timeStamp, String message) {
        super(source);
        this.timeStamp = timeStamp;
        this.message = message;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public String getMessage() {
        return message;
    }
}
