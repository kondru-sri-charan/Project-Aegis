package com.example.demo.dto;

import lombok.Data;

@Data
public class OtpVerificationDto {
    private String userId;
    private String otp;
}
