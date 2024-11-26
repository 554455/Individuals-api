package com.umaraliev.individualsapi.repository;

import com.umaraliev.individualsapi.model.User;
import org.keycloak.Token;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository {
    Token createNewUser(User user);
}
