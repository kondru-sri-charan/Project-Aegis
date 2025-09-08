package com.example.demo.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitingService {

    // Using a simple in-memory map to store buckets by API key.
    // For a distributed system, this map would be backed by a distributed cache like Redis.
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String apiKey) {
        // computeIfAbsent ensures that the bucket is created only once for each key.
        return cache.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        // Define the rate limit: 10 requests per minute.
        Bandwidth limit = Bandwidth.classic(10, Refill.greedy(10, Duration.ofMinutes(1)));
        
        // Create a new bucket with the defined limit.
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}