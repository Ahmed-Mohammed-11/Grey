package com.software.grey.models.dtos;

import com.software.grey.models.enums.Feeling;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import java.util.List;

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

    @Range(min = 1, max = 31, message = "the day must range from day 1 to 31")
    private Integer day;

    @Range(min = 1, max = 12, message = "the month must range from month 1 to 12")
    private Integer month;

    private Integer year;

    @Size(min = 0, max = 3, message = "number of feeling to filter with must be in range 0 to 3 feeling")
    private List<Feeling> feelings;

}
