package com.umaraliev.individualsapi.mapper;

import com.umaraliev.common.dto.ResponseIndividualDTO;
import com.umaraliev.individualsapi.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {

    User toUserEntity(ResponseIndividualDTO responseIndividualDTO);

    ResponseIndividualDTO toUserDto(User user);
}