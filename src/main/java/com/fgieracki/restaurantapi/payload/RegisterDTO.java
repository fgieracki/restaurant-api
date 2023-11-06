package com.fgieracki.restaurantapi.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO {
    @NotEmpty
    private String name;
    @NotEmpty
    @Length(min=6, max = 24, message = "Username must be between 6 and 24 characters")
    private String username;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Length(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
    private String password;
}
