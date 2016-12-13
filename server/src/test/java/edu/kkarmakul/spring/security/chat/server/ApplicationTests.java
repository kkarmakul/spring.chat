package edu.kkarmakul.spring.security.chat.server;

import edu.kkarmakul.spring.security.chat.test.common.AuthenticationError;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testMessageUnauthorized() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String msg = "abracadabra";
        AuthenticationError result = restTemplate.getForObject(
                "/message?text=" + msg, AuthenticationError.class);
        Assert.assertEquals(
                new AuthenticationError("unauthorized", "Full authentication is required to access this resource"),
                result);
    }

    @Ignore
    @Test
    public void testMessage() throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String msg = "abracadabra";
        byte[] result = restTemplate.getForObject(
                "/message?text=" + msg, byte[].class);
        Assert.assertArrayEquals(messageDigest.digest(msg.getBytes()), result);
    }
}
