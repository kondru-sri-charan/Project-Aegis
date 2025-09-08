package com.example.demo.dto;

import lombok.Data;

@Data // Lombok annotation to automatically generate getters, setters, constructors, etc.
public class PasswordRequestDto {
    private int length = 16; // Default length if not provided
    private boolean includeUppercase = true;
    private boolean includeNumbers = true;
    private boolean includeSymbols = true;
}