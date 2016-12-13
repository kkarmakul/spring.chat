package edu.kkarmakul.spring.security.chat.server.service.impl;

import edu.kkarmakul.spring.security.chat.server.controller.ChatController;
import edu.kkarmakul.spring.security.chat.server.dao.ChatMessageRepository;
import edu.kkarmakul.spring.security.chat.server.domain.ChatMessage;
import edu.kkarmakul.spring.security.chat.server.service.ChatService;
import edu.kkarmakul.spring.security.chat.server.service.MessageArrivedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.Arrays;
import java.util.prefs.Preferences;

@Service
public class ChatServiceImpl implements ChatService {
    public static final String RECENT_KEY = "recent";
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public void addMessage(String message) {
        ChatMessage msg = new ChatMessage(message);
        chatMessageRepository.save(msg);
        publisher.publishEvent(new MessageArrivedEvent(
                this, msg.getTimestamp(), msg.getMessage()));
    }

    @Override
    public Iterable<ChatMessage> getRecent() {
        Preferences preferences = Preferences
                .userNodeForPackage(ChatController.class);
        long last = preferences.getLong(RECENT_KEY, 0);

        ChatMessage[] messages = chatMessageRepository
                .findByIdGreaterThanOrderById(last);

        if (messages.length > 0) {
            last = messages[messages.length-1].getId();
            preferences.putLong(RECENT_KEY, last);
        }
        return Arrays.asList(messages);
    }

    @Override
    @Transactional
    public void addMessages(String[] messages) {
        for (String msg : messages) {
            chatMessageRepository.saveNew(new ChatMessage(msg));
        }
    }
}
