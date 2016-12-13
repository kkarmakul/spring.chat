package edu.kkarmakul.spring.security.chat.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Component
@SpringBootApplication
@EnableScheduling
public class ClientApp implements CommandLineRunner {
    @Autowired
    private MessageDeliveryService messageSender;

    @Autowired
    private ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        for (String line; (line = in.readLine()) != null; ) {
            messageSender.send(line);
        }
        SpringApplication.exit(applicationContext);
    }

}
