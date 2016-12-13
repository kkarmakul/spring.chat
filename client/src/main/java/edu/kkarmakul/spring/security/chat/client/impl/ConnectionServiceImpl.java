package edu.kkarmakul.spring.security.chat.client.impl;

import edu.kkarmakul.spring.security.chat.client.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class ConnectionServiceImpl implements ConnectionService {
    private static final Logger log = LoggerFactory.getLogger(ConnectionService.class);

    private final AtomicBoolean connected = new AtomicBoolean(false);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${server.port}")
    private int port;
    @Value("${ru.issreshetnev.edu.chat.server.login}")
    private String loginUrl;

    @Override
    public boolean isConnected() {
        return connected.get();
    }

    @Scheduled(fixedDelay = 500)
    public void establishConnection() {
        if (isConnected()) {
            return;
        }
        try {
            MultiValueMap<String, String> form = new LinkedMultiValueMap<String, String>();
            form.set("port", Integer.toString(port));
            ResponseEntity<String> entity = this.restTemplate.exchange(
                    loginUrl,
                    HttpMethod.POST,
                    new HttpEntity<>(form, new HttpHeaders()), String.class);
            String reply = entity.getBody();
            log.info("Connection reply from server: \"{}\"", reply);
            connected.set("OK".equals(reply));
        } catch (Exception e) {
            log.error("Failed to connect to server: {}", e.getMessage());
        }
    }
}
