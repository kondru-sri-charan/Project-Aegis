package com.example.demo.service;

import com.example.demo.dto.PasswordRequestDto;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service // Marks this class as a Spring service component
public class PasswordService {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SYMBOLS = "!@#$%^&*()-_=+<>?";
    private static final SecureRandom random = new SecureRandom();

    public String generateSecurePassword(PasswordRequestDto request) {
        StringBuilder charPool = new StringBuilder(LOWERCASE);
        List<Character> passwordChars = new ArrayList<>();

        // Guarantee at least one lowercase character
        passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));

        if (request.isIncludeUppercase()) {
            charPool.append(UPPERCASE);
            // Guarantee at least one uppercase character
            passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        }
        if (request.isIncludeNumbers()) {
            charPool.append(NUMBERS);
            // Guarantee at least one number
            passwordChars.add(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
        }
        if (request.isIncludeSymbols()) {
            charPool.append(SYMBOLS);
            // Guarantee at least one symbol
            passwordChars.add(SYMBOLS.charAt(random.nextInt(SYMBOLS.length())));
        }

        // Fill the rest of the password length with random characters from the pool
        for (int i = passwordChars.size(); i < request.getLength(); i++) {
            passwordChars.add(charPool.charAt(random.nextInt(charPool.length())));
        }

        // Shuffle the characters to ensure randomness
        Collections.shuffle(passwordChars, random);

        StringBuilder password = new StringBuilder();
        for (Character ch : passwordChars) {
            password.append(ch);
        }

        return password.toString();
    }
}