package com.fgieracki.restaurantapi.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDTO {
    private Long categoryId;

    @NotBlank(message = "Category name cannot be blank")
    private String name;
}
