package com.fgieracki.restaurantapi.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReorderDTO {
    @NotNull
    private Long itemId;
    @NotNull
    private Long beforeItemId;
}
