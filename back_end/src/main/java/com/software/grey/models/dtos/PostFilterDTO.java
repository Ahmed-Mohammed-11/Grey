package com.software.grey.models.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class PostFilterDTO {

    @Min(value = 1, message = "page size must be at least 1")
    @NotNull(message = "page size must not be null")
    private Integer pageSize;

    @Min(value = 0,message = "page number must be at least 0")
    @NotNull(message = "page number must not be null")
    private Integer pageNumber;

}
