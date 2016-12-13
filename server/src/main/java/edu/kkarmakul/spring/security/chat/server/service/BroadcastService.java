package edu.kkarmakul.spring.security.chat.server.service;

/**
 * Created by user on 18.11.16.
 */
public interface BroadcastService {
    void addClient(String ip, int port);
}
