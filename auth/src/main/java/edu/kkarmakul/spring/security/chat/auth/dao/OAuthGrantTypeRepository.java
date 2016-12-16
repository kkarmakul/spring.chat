package edu.kkarmakul.spring.security.chat.auth.dao;

import edu.kkarmakul.spring.security.chat.auth.domain.OAuthGrantType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthGrantTypeRepository extends CrudRepository<OAuthGrantType, String> {

}
