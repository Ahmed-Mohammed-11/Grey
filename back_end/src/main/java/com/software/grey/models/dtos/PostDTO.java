package com.software.grey.models.dtos;

import com.software.grey.utils.ErrorMessages;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PostDTO extends BasicDTO{

    @Size(min = 1, max = 5000, message = ErrorMessages.POST_LENGTH_LIMIT)
    private String postText;

    private UUID userId;
}
