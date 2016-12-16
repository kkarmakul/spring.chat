package edu.kkarmakul.spring.security.chat.auth.dao;

import edu.kkarmakul.spring.security.chat.auth.domain.OAuthScope;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthScopeRepository extends CrudRepository<OAuthScope, String> {

}
