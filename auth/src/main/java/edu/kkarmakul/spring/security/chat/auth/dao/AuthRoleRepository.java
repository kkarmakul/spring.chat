package edu.kkarmakul.spring.security.chat.auth.dao;

import edu.kkarmakul.spring.security.chat.auth.domain.AuthRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthRoleRepository extends CrudRepository<AuthRole, String> {

}
