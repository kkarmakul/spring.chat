package edu.kkarmakul.spring.security.chat.client.impl;

import edu.kkarmakul.spring.security.chat.client.ConnectionService;
import edu.kkarmakul.spring.security.chat.client.MessageDeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.security.MessageDigest;
import java.util.Arrays;

@Service
public class MessageDeliveryServiceImpl implements MessageDeliveryService {
    private static final Logger log = LoggerFactory.getLogger(MessageDeliveryServiceImpl.class);
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    MessageDigest messageDigest;
    @Autowired
    ConnectionService connectionService;
    @Value("${ru.issreshetnev.edu.chat.server.message}")
    private String messageUrl;

    @Override
    public void send(String line) {
        if (!connectionService.isConnected()) {
            log.error("No connection yet!");
        }
        if (!doSendMessage(line)) {
            log.error("Failed: " + line);
        }
    }

    private boolean doSendMessage(String line) {
        byte[] checksum = messageDigest.digest(line.getBytes());
        for (int i = 10; i >= 0; --i) {
            byte[] reply = restTemplate.postForObject(messageUrl, line, byte[].class);
            if (Arrays.equals(reply, checksum)) {
                return true;
            }
        }
        return false;
    }
}