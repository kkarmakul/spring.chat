package edu.kkarmakul.spring.security.chat.test.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Конечно, можно было вместо создания этого класса задействовать
 * org.springframework.security.oauth2.common.exceptions.OAuth2Exception
 * и/или его потомков, но тогда мы рискуем не "отловить" изменение в API Spring Security OAuth2
 */
public final class AuthenticationError {
    private final String error;
    @JsonProperty("error_description")
    private final String errorDescription;

    @JsonCreator
    public AuthenticationError(
            @JsonProperty("error") String error,
            @JsonProperty("error_description") String errorDescription
    ) {
        this.error = error;
        this.errorDescription = errorDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        AuthenticationError that = (AuthenticationError) o;

        if (!error.equals(that.error)) { return false; }
        return errorDescription.equals(that.errorDescription);
    }

    @Override
    public int hashCode() {
        int result = error.hashCode();
        result = 31 * result + errorDescription.hashCode();
        return result;
    }
}