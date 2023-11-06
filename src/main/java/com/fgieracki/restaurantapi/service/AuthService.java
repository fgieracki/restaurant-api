package com.fgieracki.restaurantapi.service;

import com.fgieracki.restaurantapi.payload.LoginDTO;
import com.fgieracki.restaurantapi.payload.RegisterDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);

    String register(RegisterDTO registerDTO);
}
