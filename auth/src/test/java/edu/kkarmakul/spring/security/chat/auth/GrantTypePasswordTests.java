package edu.kkarmakul.spring.security.chat.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.kkarmakul.spring.security.chat.auth.dao.AuthRoleRepository;
import edu.kkarmakul.spring.security.chat.auth.dao.OAuthClientDetailsRepository;
import edu.kkarmakul.spring.security.chat.auth.dao.OAuthGrantTypeRepository;
import edu.kkarmakul.spring.security.chat.auth.dao.OAuthResourceRepository;
import edu.kkarmakul.spring.security.chat.auth.dao.OAuthScopeRepository;
import edu.kkarmakul.spring.security.chat.auth.domain.AuthRole;
import edu.kkarmakul.spring.security.chat.auth.domain.OAuthClientDetails;
import edu.kkarmakul.spring.security.chat.auth.domain.OAuthGrantType;
import edu.kkarmakul.spring.security.chat.auth.domain.OAuthResource;
import edu.kkarmakul.spring.security.chat.auth.domain.OAuthScope;
import edu.kkarmakul.spring.security.chat.test.common.AuthenticationError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.internal.util.collections.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class GrantTypePasswordTests {

    private static final String HAL_JSON_VALUE = "application/hal+json";
    private static final String HAL_JSON_UTF8_VALUE = APPLICATION_JSON_UTF8_VALUE.replace(APPLICATION_JSON_VALUE, HAL_JSON_VALUE);
    private static final Pattern TOKEN_REGEXP = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private FilterChainProxy filterChain;
    @Autowired
    private OAuthClientDetailsRepository clientDetailsRepository;
    @Autowired
    private OAuthResourceRepository resourceRepository;
    @Autowired
    private OAuthScopeRepository scopeRepository;
    @Autowired
    private OAuthGrantTypeRepository grantTypeRepository;
    @Autowired
    private AuthRoleRepository roleRepository;

    private MockMvc mvc;

    @Before
    public void setUp() {
        this.mvc = webAppContextSetup(context).addFilters(filterChain).build();
        SecurityContextHolder.clearContext();
        createClients();
    }

    private void createClients() {
        OAuthClientDetails clientDetails = new OAuthClientDetails();
        clientDetails.setClientId("oauthClientId");
        clientDetails.setClientSecret("oauthClientSecret");
        clientDetails.setResources(Collections.singleton(
                createResource("messages", "A resource containing chat messages")));
        clientDetails.setScopes(Collections.singleton(
                createScope("read")));
        clientDetails.setRoles(Collections.singleton(
                createRole("ROLE_CLIENT")));
        clientDetails.setGrantTypes(Sets.newSet(
                createGrantType("password"),
                createGrantType("refresh_token")));
        clientDetailsRepository.save(clientDetails);
    }

    private OAuthResource createResource(String id, String description) {
        OAuthResource resource = new OAuthResource();
        resource.setId(id);
        resource.setDescription(description);
        resourceRepository.save(resource);
        return resource;
    }

    private OAuthScope createScope(String id) {
        OAuthScope scope = new OAuthScope();
        scope.setId(id);
        scopeRepository.save(scope);
        return scope;
    }

    private AuthRole createRole(String id) {
        AuthRole role = new AuthRole();
        role.setId(id);
        roleRepository.save(role);
        return role;
    }

    private OAuthGrantType createGrantType(String id) {
        OAuthGrantType grantType = new OAuthGrantType();
        grantType.setId(id);
        grantTypeRepository.save(grantType);
        return grantType;
    }

    @Test
    public void justBasicAuthorization() throws Exception {
        String header = "Basic " + new String(Base64.encode("oauthClientId:oauthClientSecret".getBytes()));
        mvc.perform(post("/oauth/token").header("Authorization", header))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(content().json(objectMapper.writeValueAsString(
                        new AuthenticationError("invalid_scope", "Missing grant type")
                )));
    }

    @Test
    public void useAppSecretsPlusUserAccountToGetBearerToken() throws Exception {
        String header = "Basic " + new String(Base64.encode("oauthClientId:oauthClientSecret".getBytes()));
        MvcResult result = mvc
                .perform(post("/oauth/token").header("Authorization", header)
                        .param("grant_type", "password").param("scope", "read")
                        .param("username", "login").param("password", "password"))
                .andDo(print()).andExpect(status().isOk()).andReturn();

        StringMap answer = objectMapper.readValue(result.getResponse().getContentAsString(), StringMap.class);
        assertToken(answer.get("access_token"));
        assertToken(answer.get("refresh_token"));
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

    private static void assertToken(CharSequence token) {
        assertTrue(TOKEN_REGEXP.matcher(token).matches());
    }

    private static class StringMap extends HashMap<String, String> {

        private static final long serialVersionUID = 5661052487899396349L;
    }
}
