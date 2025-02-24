package com.umaraliev.individualsapi.service;

import com.umaraliev.common.dto.IndividualDTO;
import com.umaraliev.common.dto.ResponseIndividualDTO;
import com.umaraliev.individualsapi.mapper.UserMapper;
import com.umaraliev.individualsapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class RequestPersonAPIService {

    private final RestTemplate restTemplate;
    private final UserMapper userMapper;

    @Value("${person.api.registration-url}")
    private String urlRegistrationUserPersonAPIService;

    @Value("${person.api.remove-url}")
    private String urlRemoveUserPersonAPIService;

    public User requestRegistrationUserPersonAPI(IndividualDTO IndividualDTO) {
        ResponseEntity<ResponseIndividualDTO> responseIndividualDTOResponseEntity;

        try {
            responseIndividualDTOResponseEntity = restTemplate.postForEntity(
                    urlRegistrationUserPersonAPIService,
                    IndividualDTO,
                    ResponseIndividualDTO.class
            );
        }catch (Exception e) {
            throw new RuntimeException("An error occurred when requesting registration in the person-api microservice" + e.getMessage());
        }
        ResponseIndividualDTO responseIndividualDTO = responseIndividualDTOResponseEntity.getBody();

        return userMapper.toUserEntity(responseIndividualDTO);
    }

    public void requestRemoveUserPersonAPI(User user) {
        restTemplate.delete(urlRegistrationUserPersonAPIService + "/" + user.getId());
    }
}
