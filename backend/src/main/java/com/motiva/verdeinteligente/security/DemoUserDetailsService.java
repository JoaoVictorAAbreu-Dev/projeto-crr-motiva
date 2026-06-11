package com.motiva.verdeinteligente.security;

import com.motiva.verdeinteligente.dto.AuthenticatedUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DemoUserDetailsService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;

    @Value("${app.auth.demo-user.username}")
    private String username;

    @Value("${app.auth.demo-user.password}")
    private String password;

    @Value("${app.auth.demo-user.full-name}")
    private String fullName;

    @Value("${app.auth.demo-user.role}")
    private String role;

    public DemoUserDetailsService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String requestedUsername) throws UsernameNotFoundException {
        if (!username.equalsIgnoreCase(requestedUsername)) {
            throw new UsernameNotFoundException("User not found: " + requestedUsername);
        }

        return User.withUsername(username)
            .password(passwordEncoder.encode(password))
            .roles(role)
            .build();
    }

    public AuthenticatedUserResponse currentUser() {
        return new AuthenticatedUserResponse(username, fullName, role);
    }
}
