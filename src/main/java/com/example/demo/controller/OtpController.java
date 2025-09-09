package com.example.demo.controller;

import com.example.demo.dto.GenericResponseDto;
import com.example.demo.dto.OtpRequestDto;
import com.example.demo.dto.OtpVerificationDto;
import com.example.demo.service.OtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/otp")
public class OtpController {

    private final OtpService otpService;

    public OtpController(OtpService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/generate")
    public ResponseEntity<GenericResponseDto> generateOtp(@RequestBody OtpRequestDto otpRequest) {
        otpService.generateOtp(otpRequest.getIdentifier());
        // For security, we don't send the OTP back in the response.
        // The user gets it via email/SMS.
        return ResponseEntity.ok(new GenericResponseDto("success", "OTP has been generated and sent."));
    }

    @PostMapping("/verify")
    public ResponseEntity<GenericResponseDto> verifyOtp(@RequestBody OtpVerificationDto verificationDto) {
        boolean isValid = otpService.verifyOtp(verificationDto.getIdentifier(), verificationDto.getOtp());

        if (isValid) {
            return ResponseEntity.ok(new GenericResponseDto("success", "OTP verified successfully."));
        } else {
            return ResponseEntity.status(400).body(new GenericResponseDto("error", "Invalid or expired OTP."));
        }
    }
}