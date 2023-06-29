package com.fitit100.village.services;


import com.fitit100.village.models.AppUser;
import com.fitit100.village.models.LoginResponseDTO;
import com.fitit100.village.models.Role;
import com.fitit100.village.repository.RoleRepository;
import com.fitit100.village.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationService {
    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    public AppUser registerUser(String username, String password) {
        String encodePassword = passwordEncoder.encode(password);
        Role userRole = roleRepository.findByAuthority("USER").get();
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(userRole);

        AppUser user = new AppUser();
        user.setUserId(0);
        user.setUsername(username);
        user.setPassword(encodePassword);
        user.setAuthorities(roleSet);
        return userRepository.save(user);
    }

    public LoginResponseDTO loginUser(String username, String password) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            String token = tokenService.generateJwt(auth);
            return new LoginResponseDTO(userRepository.findByUsername(username).get(), token);
        } catch (AuthenticationException ex) {
            return new LoginResponseDTO(null, "");
        }
    }
}
