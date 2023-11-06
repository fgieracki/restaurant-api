package com.fgieracki.restaurantapi.config;

import com.fgieracki.restaurantapi.model.Role;
import com.fgieracki.restaurantapi.model.User;
import com.fgieracki.restaurantapi.repository.RoleRepository;
import com.fgieracki.restaurantapi.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class Init {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initDB() {
        Role admin = addRole("ROLE_ADMIN");
        addRole("ROLE_USER");
        addAdmin(admin);
        log.info("init finished");
    }

    private Role addRole(String roleName) {
        try{
            return roleRepository.save(new Role(roleName));
        } catch (Exception e){
            log.warn("Role {} already exists", roleName);
            return roleRepository.findByName(roleName).orElseThrow(
                    RuntimeException::new
            );
        }
    }

    private void addAdmin(Role adminRole) {
        User admin = new User();
        admin.addRole(adminRole);
        admin.setName("admin");
        admin.setEmail("admin@fgieracki.com");
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        try {
            userRepository.save(admin);
        } catch (Exception e) {
            log.warn("Admin already exists");
        }
    }
}
