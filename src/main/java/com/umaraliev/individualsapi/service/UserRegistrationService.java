package com.umaraliev.individualsapi.service;

import com.umaraliev.individualsapi.model.NewUserRecord;
import com.umaraliev.individualsapi.model.User;
import com.umaraliev.individualsapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.keycloak.Token;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRegistrationService implements UserRepository{


    @Override
    public Token createNewUser(User user) {


        return null;
    }
}
