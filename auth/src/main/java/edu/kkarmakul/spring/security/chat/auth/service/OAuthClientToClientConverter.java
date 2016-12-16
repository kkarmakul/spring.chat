package edu.kkarmakul.spring.security.chat.auth.service;

import edu.kkarmakul.spring.security.chat.auth.domain.AuthRole;
import edu.kkarmakul.spring.security.chat.auth.domain.OAuthClientDetails;
import edu.kkarmakul.spring.security.chat.auth.domain.OAuthGrantType;
import edu.kkarmakul.spring.security.chat.auth.domain.OAuthResource;
import edu.kkarmakul.spring.security.chat.auth.domain.OAuthScope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;

import java.util.stream.Collector;
import java.util.stream.Collectors;

public class OAuthClientToClientConverter implements Converter<OAuthClientDetails, ClientDetails> {

    @Override
    public ClientDetails convert(final OAuthClientDetails clientDetails) {
        return new CustomClientDetails(clientDetails);
    }

    public static class CustomClientDetails extends BaseClientDetails {

        private static final long serialVersionUID = -821855362458389304L;
        private static final Collector<CharSequence, ?, String> COLLECTOR = Collectors.joining(",");

        public CustomClientDetails(OAuthClientDetails clientDetails) {
            super(clientDetails.getClientId(),
                    clientDetails.getResources()
                            .stream().map(OAuthResource::getId).collect(COLLECTOR),
                    clientDetails.getScopes()
                            .stream().map(OAuthScope::getId).collect(COLLECTOR),
                    clientDetails.getGrantTypes()
                            .stream().map(OAuthGrantType::getId).collect(COLLECTOR),
                    clientDetails.getRoles()
                            .stream().map(AuthRole::getId).collect(COLLECTOR));
            setClientSecret(clientDetails.getClientSecret());
        }
    }
}
