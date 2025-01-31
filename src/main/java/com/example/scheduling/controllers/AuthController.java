package com.example.scheduling.controllers;

import com.example.scheduling.dto.AuthDTO;
import com.example.scheduling.dto.AuthResponseDTO;
import com.example.scheduling.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody AuthDTO request) {

            return ResponseEntity.ok(authService.register(request));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthDTO request) {

            return ResponseEntity.ok(authService.login(request));

    }
}
