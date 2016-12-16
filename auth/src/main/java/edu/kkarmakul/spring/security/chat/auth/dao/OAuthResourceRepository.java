package edu.kkarmakul.spring.security.chat.auth.dao;

import edu.kkarmakul.spring.security.chat.auth.domain.OAuthResource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthResourceRepository extends CrudRepository<OAuthResource, String> {

}
