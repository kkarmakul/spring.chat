package edu.kkarmakul.spring.security.chat.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kkarmakul.spring.security.chat.test.common.AuthenticationError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class GrantTypePasswordTests {

    private static final String HAL_JSON_VALUE = "application/hal+json";
    private static final String HAL_JSON_UTF8_VALUE = APPLICATION_JSON_UTF8_VALUE.replace(APPLICATION_JSON_VALUE, HAL_JSON_VALUE);

    @Autowired
    WebApplicationContext context;

    @Autowired
    FilterChainProxy filterChain;

    private MockMvc mvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private Pattern tokenRegExp = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(context).addFilters(filterChain).build();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void justBasicAuthorization() throws Exception {
        String header = "Basic " + new String(Base64.encode("oauthClientId:oauthClientSecret".getBytes()));
        mvc.perform(post("/oauth/token").header("Authorization", header))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new AuthenticationError("invalid_scope", "Empty scope (either the client or the user is not allowed the requested scopes)")
                )));
    }

    @Test
    public void useAppSecretsPlusUserAccountToGetBearerToken() throws Exception {
        String header = "Basic " + new String(Base64.encode("oauthClientId:oauthClientSecret".getBytes()));
        MvcResult result = mvc
                .perform(post("/oauth/token").header("Authorization", header)
                        .param("grant_type", "password").param("scope", "read")
                        .param("username", "login").param("password", "password"))
                .andExpect(status().isOk()).andReturn();

        Map answer = objectMapper.readValue(result.getResponse().getContentAsString(), Map.class);
        assertTrue(tokenRegExp.matcher((CharSequence) answer.get("access_token")).matches());
        assertTrue(tokenRegExp.matcher((CharSequence) answer.get("refresh_token")).matches());
        assertEquals("bearer", answer.get("token_type"));
        assertEquals("read", answer.get("scope"));
        /*
        MvcResult flightsAction = this.mvc
                .perform(get("/flights/1").accept(HAL_JSON_VALUE)
                        .header("Authorization", "Bearer " + accessToken))
                .andExpect(header().string("Content-Type",
                        HAL_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(status().isOk()).andDo(print()).andReturn();

        Flight flight = this.objectMapper.readValue(
                flightsAction.getResponse().getContentAsString(), Flight.class);

        assertThat(flight.getOrigin()).isEqualTo("Nashville");
        assertThat(flight.getDestination()).isEqualTo("Dallas");
        assertThat(flight.getAirline()).isEqualTo("Spring Ways");
        assertThat(flight.getFlightNumber()).isEqualTo("OAUTH2");
        assertThat(flight.getTraveler()).isEqualTo("Greg Turnquist");
        */
    }
}
