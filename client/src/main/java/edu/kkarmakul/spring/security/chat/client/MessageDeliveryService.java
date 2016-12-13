package edu.kkarmakul.spring.security.chat.client;

/**
 * Created by user on 18.11.16.
 */
public interface MessageDeliveryService {
    void send(String line);
}
