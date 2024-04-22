package com.backend.task.infrastructure.rest.controller;

import com.backend.task.application.service.AuthService;
import com.backend.task.domain.model.dto.request.AuthRequestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthRequestResponse> signUp(@RequestBody AuthRequestResponse signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
    @PostMapping("/signin")
    public ResponseEntity<AuthRequestResponse> signIn(@RequestBody AuthRequestResponse signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
}
