package com.software.grey.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class RecommendedPostsDTO {

    private List<PostDTO> content;
    private boolean last;
}
