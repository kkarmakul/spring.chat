package edu.kkarmakul.spring.security.chat.server.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class ChecksumConfig {
    @Bean
    public static MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance("MD5");
    }

}
