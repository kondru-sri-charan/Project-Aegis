package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor      // Lombok: generates a no-argument constructor
@AllArgsConstructor     // Lombok: generates a constructor with all arguments
public class PasswordResponseDto {
    private String password;
}