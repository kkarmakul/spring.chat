package edu.kkarmakul.spring.security.chat.auth.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import java.util.Set;

@Entity
public class OAuthClientDetails {

    @Id
    private String clientId;

    @Column
    private String clientSecret;

    @ManyToMany
    private Set<OAuthResource> resources;

    @ManyToMany
    private Set<OAuthScope> scopes;

    @ManyToMany
    private Set<OAuthGrantType> grantTypes;

    @ManyToMany
    private Set<AuthRole> roles;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public Set<OAuthResource> getResources() {
        return resources;
    }

    public void setResources(Set<OAuthResource> resources) {
        this.resources = resources;
    }

    public Set<OAuthScope> getScopes() {
        return scopes;
    }

    public void setScopes(Set<OAuthScope> scopes) {
        this.scopes = scopes;
    }

    public Set<OAuthGrantType> getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(Set<OAuthGrantType> grantTypes) {
        this.grantTypes = grantTypes;
    }

    public Set<AuthRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<AuthRole> roles) {
        this.roles = roles;
    }
}
