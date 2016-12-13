package edu.kkarmakul.spring.security.chat.server.service.impl;

import edu.kkarmakul.spring.security.chat.server.service.BroadcastService;
import edu.kkarmakul.spring.security.chat.server.service.MessageArrivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

@Service
public class BroadcastServiceImpl implements ApplicationListener<MessageArrivedEvent>, BroadcastService {
    private static final Logger log =
            LoggerFactory.getLogger(BroadcastServiceImpl.class);

    Set<String> clients = new ConcurrentSkipListSet<>();

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void onApplicationEvent(MessageArrivedEvent event) {
        log.debug("New message arrived at {}: \"{}\"",
                event.getTimeStamp(), event.getMessage());
        List<String> disconnectedClients = new ArrayList<>(0);
        for (String host : clients) {
            try {
                restTemplate.postForLocation(
                        "http://" + host + "/message",
                        event.getMessage());
                log.debug("Transmited to: {}", host);
            } catch (Exception e) {
                log.error("Failed to transmit to: {}. {}", host, e.getMessage());
                disconnectedClients.add(host);
            }
        }
        clients.removeAll(disconnectedClients);
        log.info("New message broadcasted: \"{}\"", event.getMessage());
    }

    @Override
    public void addClient(String ip, int port) {
        clients.add(ip + ':' + port);
        log.info("New client was registered: {}:{}", ip, port);
    }
}
