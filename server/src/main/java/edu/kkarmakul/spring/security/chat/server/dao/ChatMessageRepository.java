package edu.kkarmakul.spring.security.chat.server.dao;

import edu.kkarmakul.spring.security.chat.server.domain.ChatMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository
        extends CrudRepository<ChatMessage, Long>,
        ChatMessageRepositoryCustom
{
}
