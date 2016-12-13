package edu.kkarmakul.spring.security.chat.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MessageController {
    @ResponseBody
    @PostMapping("message")
    public void getMessage(@RequestBody String message) {
        System.out.println(message);
    }
}
