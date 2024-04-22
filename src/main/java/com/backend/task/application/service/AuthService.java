package com.backend.task.application.service;

import com.backend.task.domain.model.dto.request.AuthRequestResponse;
import com.backend.task.infrastructure.adapter.entity.UserEntity;
import com.backend.task.infrastructure.adapter.repository.UserRepository;
import com.backend.task.infrastructure.config.JwtUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LogManager.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthRequestResponse signUp(AuthRequestResponse request){
        AuthRequestResponse response = new AuthRequestResponse();
        try {
            UserEntity user = new UserEntity();
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            UserEntity result = userRepository.save(user);
            if (result.getId() > 0){
                response.setMessage("Usuario almacenado de manera correcta.");
                response.setStatusCode(200);
            }
        }
        catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public AuthRequestResponse signIn(AuthRequestResponse request){
        AuthRequestResponse response = new AuthRequestResponse();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            log.info("El usuario es: {}", user.getUsername());
            var jwt = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setMessage("Login exitoso.");
        } catch (Exception e){
            log.error(e.getMessage());
            response.setStatusCode(500);
            response.setError("Error al intentar logearse.");
        }
        return response;
    }
}
