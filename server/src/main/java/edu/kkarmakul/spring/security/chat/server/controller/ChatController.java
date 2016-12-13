package edu.kkarmakul.spring.security.chat.server.controller;

import edu.kkarmakul.spring.security.chat.server.domain.ChatMessage;
import edu.kkarmakul.spring.security.chat.server.service.BroadcastService;
import edu.kkarmakul.spring.security.chat.server.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import java.security.MessageDigest;

@RestController
public class ChatController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageDigest messageDigest;
    @Autowired
    BroadcastService broadcastService;

    @PostMapping("message")
    public byte[] addMessage(@RequestBody String message) {
        chatService.addMessage(message);
        return messageDigest.digest(message.getBytes());
    }

    @PostMapping("login")
    public String addMessages(
            @RequestParam(name = "port") int port, HttpServletRequest request) {
        broadcastService.addClient(request.getRemoteAddr(), port);
        return "OK";
    }

    @RequestMapping("recent")
    public String recent() {
        StringBuilder sb = new StringBuilder();
        for (ChatMessage chatMessage : chatService.getRecent()) {
            sb.append(chatMessage.getMessage())
                    .append(System.lineSeparator());
        }
        return sb.toString();
    }
}
