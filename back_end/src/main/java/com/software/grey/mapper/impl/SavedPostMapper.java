package com.software.grey.mapper.impl;

import com.software.grey.mapper.Mapper;
import com.software.grey.models.dtos.SavedPostDto;
import com.software.grey.models.entities.SavedPost;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SavedPostMapper implements Mapper<SavedPost, SavedPostDto> {

    ModelMapper modelMapper;

    @Autowired
    SavedPostMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public SavedPostDto mapTo(SavedPost entity) {
        return modelMapper.map(entity, SavedPostDto.class);
    }

    @Override
    public SavedPost mapFrom(SavedPostDto dto) {
        return modelMapper.map(dto, SavedPost.class);
    }
}
