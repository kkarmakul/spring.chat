package edu.kkarmakul.spring.security.chat.auth.dao;

import edu.kkarmakul.spring.security.chat.auth.domain.OAuthClientDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthClientDetailsRepository extends CrudRepository<OAuthClientDetails, String> {

}
