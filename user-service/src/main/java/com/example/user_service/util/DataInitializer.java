package com.example.user_service.util;

import com.example.user_service.entity.Role;
import com.example.user_service.entity.User;
import com.example.user_service.repository.RoleRepository;
import com.example.user_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DataInitializer implements ApplicationRunner {

    RoleRepository roleRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) {
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().roleName("ADMIN").build());
            roleRepository.save(Role.builder().roleName("USER").build());
        }
        if (userRepository.count() == 0) {
            userRepository.save(User.builder()
                            .username("admin")
                            .password(passwordEncoder.encode("123456"))
                            .build());
        }
    }
}
