package com.example.demo.service;

import com.example.demo.model.ApiKey;
import com.example.demo.repository.ApiKeyRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;
    private final PasswordEncoder passwordEncoder;
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    public ApiKeyService(ApiKeyRepository apiKeyRepository, PasswordEncoder passwordEncoder) {
        this.apiKeyRepository = apiKeyRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateApiKey(String userId) {
        byte[] randomBytes = new byte[24];
        secureRandom.nextBytes(randomBytes);
        String rawApiKey = base64Encoder.encodeToString(randomBytes);

        String apiKeyHash = passwordEncoder.encode(rawApiKey);

        ApiKey apiKey = new ApiKey();
        apiKey.setUserId(userId);
        apiKey.setApiKeyHash(apiKeyHash);
        apiKeyRepository.save(apiKey);

        return rawApiKey;
    }

    public boolean isValidApiKey(String rawApiKey) {
        if (rawApiKey == null || rawApiKey.isBlank()) {
        return false; // Added a null/blank check for robustness
    }
        List<ApiKey> allKeys = apiKeyRepository.findAll();
        for (ApiKey key : allKeys) {
            if (passwordEncoder.matches(rawApiKey, key.getApiKeyHash())) {
                return true;
            }
        }
        return false;
    }
}