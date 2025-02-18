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
    private String urlPersonAPIService;

    public User requestPersonAPI(IndividualDTO IndividualDTO) {
        ResponseEntity<ResponseIndividualDTO> responseIndividualDTOResponseEntity = restTemplate.postForEntity(
                urlPersonAPIService,
                IndividualDTO,
                ResponseIndividualDTO.class
        );
        ResponseIndividualDTO responseIndividualDTO = responseIndividualDTOResponseEntity.getBody();

        return userMapper.toUserEntity(responseIndividualDTO);
    }
}
