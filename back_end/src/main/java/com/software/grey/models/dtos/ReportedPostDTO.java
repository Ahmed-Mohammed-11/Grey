package com.software.grey.models.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ReportedPostDTO {

    @NotNull
    @NotEmpty(message = "So, what is the problem? Did you forget the post to report?")
    private String postId;

    @NotNull
    @NotEmpty(message = "Who reported this post?")
    private String reporterId;
}
