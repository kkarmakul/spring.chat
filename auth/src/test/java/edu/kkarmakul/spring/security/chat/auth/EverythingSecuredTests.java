package edu.kkarmakul.spring.security.chat.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EverythingSecuredTests {

    private static final String HAL_JSON_VALUE = "application/hal+json";
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private FilterChainProxy filterChain;
    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(context).addFilters(filterChain).build();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void validateRoot() throws Exception {
        validateSecured("/");
    }

    private void validateSecured(String url) throws Exception {
        mvc.perform(get(url).accept(HAL_JSON_VALUE))
                .andExpect(status().isUnauthorized())/*.andDo(print())*/;
    }

    @Test
    public void validateOauth() throws Exception {
        validateSecured("/oauth");
    }

    @Test
    public void validateOauthAuthorize() throws Exception {
        validateSecured("/oauth/authorize");
    }

    @Test
    public void validateOauthToken() throws Exception {
        validateSecured("/oauth/token");
    }

    @Test
    public void validateUser() throws Exception {
        validateSecured("/user");
    }

    @Test
    public void validateUserAdmin() throws Exception {
        validateSecured("/user/admin");
    }

    @Test
    public void validateDoesNotExist() throws Exception {
        validateSecured("/doesNotExist");
    }
}
