package edu.kkarmakul.spring.security.chat.server.service;

import edu.kkarmakul.spring.security.chat.server.domain.ChatMessage;

/**
 * Created by stu on 16.11.2016.
 */
public interface ChatService {
    void addMessage(String message);

    Iterable<ChatMessage> getRecent();

    void addMessages(String[] messages);
}
