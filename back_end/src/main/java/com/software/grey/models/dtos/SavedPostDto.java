package com.software.grey.models.dtos;

import com.software.grey.models.entities.Post;
import com.software.grey.models.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SavedPostDto {

    private User user;

    private Post post;
}
