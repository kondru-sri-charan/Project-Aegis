package com.example.demo.interceptor;

import com.example.demo.service.ApiKeyService;
import com.example.demo.service.RateLimitingService; // Import the new service
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ApiKeyAuthInterceptor implements HandlerInterceptor {

    private final ApiKeyService apiKeyService;
    private final RateLimitingService rateLimitingService; // Inject the new service
    private static final String API_KEY_HEADER = "X-API-KEY";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String apiKey = request.getHeader(API_KEY_HEADER);
        log.info("Intercepting request for {} and validating API Key.", request.getRequestURI());

        // Step 1: Authentication
        if (apiKey == null || !apiKeyService.isValidApiKey(apiKey)) {
            log.warn("API Key validation failed. Rejecting request from IP: {}", request.getRemoteAddr());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "A valid X-API-KEY header is required.");
            return false;
        }

        log.info("API Key validation successful.");

        // Step 2: Rate Limiting (only if authentication passed)
        Bucket tokenBucket = rateLimitingService.resolveBucket(apiKey);
        ConsumptionProbe probe = tokenBucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            // The request is within the limit
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            log.info("Request approved. Tokens remaining: {}", probe.getRemainingTokens());
            return true;
        } else {
            // The request exceeds the limit
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefill));
            log.warn("Rate limit exceeded for API Key: {}. Rejecting request.", apiKey.substring(0, 8) + "...");
            sendErrorResponse(response, HttpStatus.TOO_MANY_REQUESTS.value(), "Too Many Requests", "You have exhausted your API Request Quota.");
            return false;
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String error, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(String.format("{\"error\": \"%s\", \"message\": \"%s\"}", error, message));
    }
}