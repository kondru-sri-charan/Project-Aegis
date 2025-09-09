package com.example.demo.dto;

import lombok.Data;

@Data
public class OtpRequestDto {
    private String identifier; // Can be an email, username, phone number, etc.
}
