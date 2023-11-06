package com.fgieracki.restaurantapi.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagDTO {
    private Long tagId;
    private String name;
}
