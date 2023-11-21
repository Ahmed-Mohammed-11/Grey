package com.software.grey.models.mappers;

import com.software.grey.models.entities.GoogleUser;
import com.software.grey.models.entities.User;
import com.software.grey.models.dtos.UserDTO;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toUser(UserDTO postDTO, @MappingTarget User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GoogleUser toGoogleUser(UserDTO postDTO, @MappingTarget GoogleUser user);

}
