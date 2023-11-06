package com.fgieracki.restaurantapi.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.NumberFormat;

import java.util.Set;

@Data
@NoArgsConstructor
public class ItemDTO {
    private Long itemId;

    @NotNull
    @NumberFormat
    private Long code;
    @NotBlank
    private String name;
    @NotBlank
    @Length(max = 255)
    private String description;

    @NotNull
    @NumberFormat
    private Double price;

    @NotBlank
    private CategoryDTO category;
    private Set<Long> tagIds;
}
