package com.example.demo.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {

    private final StringRedisTemplate redisTemplate;
    private static final SecureRandom random = new SecureRandom();

    public OtpService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Generates a 6-digit OTP and stores it in Redis with a 5-minute TTL.
     *
     * @param userId The unique identifier for the user (e.g., email).
     * @return The generated 6-digit OTP.
     */
    public String generateOtp(String userId) {
        // Generate a 6-digit number between 100000 and 999999
        String otp = String.valueOf(100000 + random.nextInt(900000));

        // Store the OTP in Redis. The key is the userId.
        // It will automatically expire after 5 minutes. This is a critical feature.
        redisTemplate.opsForValue().set(userId, otp, 5, TimeUnit.MINUTES);

        // In a real application, you would now send this OTP via SMS or email.
        // For this project, we'll just log it to see it.
        System.out.println("Generated OTP for " + userId + ": " + otp);

        return otp;
    }

    /**
     * Verifies the provided OTP against the one stored in Redis.
     *
     * @param userId The user's identifier.
     * @param otp The OTP provided by the user.
     * @return true if the OTP is valid, false otherwise.
     */
    public boolean verifyOtp(String userId, String otp) {
        String storedOtp = redisTemplate.opsForValue().get(userId);

        if (storedOtp != null && storedOtp.equals(otp)) {
            // OTP is correct. Invalidate it by deleting it from Redis.
            redisTemplate.delete(userId);
            return true;
        }
        return false;
    }
}