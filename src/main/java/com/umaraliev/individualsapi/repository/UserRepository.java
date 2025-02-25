package com.umaraliev.individualsapi.repository;

import com.umaraliev.individualsapi.dto.AuthTokenResponse;
import com.umaraliev.individualsapi.model.User;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository {
    AuthTokenResponse createNewUser(User user);
}
