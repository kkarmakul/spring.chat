package edu.kkarmakul.spring.security.chat.auth.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class OAuthGrantType {

    @Id
    @Column(length = 16)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
