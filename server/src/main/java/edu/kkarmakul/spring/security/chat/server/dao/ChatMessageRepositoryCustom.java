package edu.kkarmakul.spring.security.chat.server.dao;

import edu.kkarmakul.spring.security.chat.server.domain.ChatMessage;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepositoryCustom {
    ChatMessage[] findByIdGreaterThanOrderById(long last);

    void saveNew(ChatMessage chatMessage);
}
