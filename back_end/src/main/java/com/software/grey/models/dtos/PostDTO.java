package com.software.grey.models.dtos;

import com.software.grey.models.enums.Feeling;
import com.software.grey.utils.ErrorMessages;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.sql.Timestamp;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PostDTO {

    private UUID id;

    @NotNull
    @Size(min = 1, max = 5000, message = ErrorMessages.POST_LENGTH_LIMIT)
    private String postText;

    @NotEmpty(message = "post feelings must be 1 at least")
    private Set<Feeling> postFeelings;

    private Timestamp postTime;
}
