package com.software.grey.models.mappers;

import com.software.grey.models.dtos.PostDTO;
import com.software.grey.models.entities.Post;
import com.software.grey.services.implementations.UserService;
import org.mapstruct.*;

import java.util.Optional;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Post toPost(PostDTO postDTO);
}
