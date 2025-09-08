package com.example.demo.controller;

import com.example.demo.dto.PasswordRequestDto;
import com.example.demo.dto.PasswordResponseDto;
import com.example.demo.service.PasswordService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Marks this class as a controller that handles REST requests
@RequestMapping("/api/v1/password") // Base URL for all endpoints in this controller
public class PasswordController {

    private final PasswordService passwordService;

    // Constructor-based dependency injection (best practice)
    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }

    @PostMapping("/generate") // Handles POST requests to /api/v1/password/generate
    public ResponseEntity<PasswordResponseDto> generatePassword(@RequestBody PasswordRequestDto request) {
        String generatedPassword = passwordService.generateSecurePassword(request);
        PasswordResponseDto response = new PasswordResponseDto(generatedPassword);
        return ResponseEntity.ok(response);
    }
}