package edu.kkarmakul.spring.security.chat.server.dao.impl;

import edu.kkarmakul.spring.security.chat.server.dao.ChatMessageRepositoryCustom;
import edu.kkarmakul.spring.security.chat.server.domain.ChatMessage;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import java.util.List;

public class ChatMessageRepositoryImpl
        implements ChatMessageRepositoryCustom
{
    @PersistenceContext
    private EntityManager em;

    @Override
    public ChatMessage[] findByIdGreaterThanOrderById(long last) {
        Query nativeQuery = em.createNativeQuery("SELECT * FROM chat_message WHERE id >= :id", ChatMessage.class);
        nativeQuery.setParameter("id", last);
        List<ChatMessage> resultList = nativeQuery.getResultList();
        return resultList.toArray(new ChatMessage[resultList.size()]);
    }

    @Override
    public void saveNew(ChatMessage chatMessage) {
        em.persist(chatMessage);
    }
}
