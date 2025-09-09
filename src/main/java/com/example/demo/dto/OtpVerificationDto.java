package com.example.demo.dto;

import lombok.Data;

@Data
public class OtpVerificationDto {
    private String identifier;
    private String otp;
}
