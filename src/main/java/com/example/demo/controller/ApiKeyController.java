package com.example.demo.controller;

import com.example.demo.dto.OtpRequestDto;
import com.example.demo.service.ApiKeyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/keys")
public class ApiKeyController {

    private final ApiKeyService apiKeyService;

    public ApiKeyController(ApiKeyService apiKeyService) {
        this.apiKeyService = apiKeyService;
    }

    @PostMapping("/generate")
    public ResponseEntity<?> generateApiKey(@RequestBody OtpRequestDto request) {
        String rawKey = apiKeyService.generateApiKey(request.getIdentifier());
        return ResponseEntity.ok(Map.of("apiKey", rawKey));
    }
}