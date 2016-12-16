package edu.kkarmakul.spring.security.chat.auth.service.impl;

import edu.kkarmakul.spring.security.chat.auth.dao.OAuthClientDetailsRepository;
import edu.kkarmakul.spring.security.chat.auth.domain.OAuthClientDetails;
import edu.kkarmakul.spring.security.chat.auth.service.CustomClientDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class OauthClientDetailsServiceImpl implements CustomClientDetailsService {

    @Autowired
    private OAuthClientDetailsRepository clientDetailsRepository;

    @Autowired
    private ConversionService conversionService;

    @Override
    @Transactional
    public ClientDetails loadClientByClientId(String clientId) {
        OAuthClientDetails client = clientDetailsRepository.findOne(clientId);
        if (client == null) {
            throw new ClientRegistrationException("No such a client: " + clientId);
        }
        return conversionService.convert(
                client,
                ClientDetails.class);
    }
}
