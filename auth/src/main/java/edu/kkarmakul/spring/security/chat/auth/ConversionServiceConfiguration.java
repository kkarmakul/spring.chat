package edu.kkarmakul.spring.security.chat.auth;

import edu.kkarmakul.spring.security.chat.auth.service.OAuthClientToClientConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ConversionServiceConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addFormatters(FormatterRegistry formatterRegistry) {
        formatterRegistry.addConverter(new OAuthClientToClientConverter());
    }
}
