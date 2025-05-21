package tech.stl.hcm.candidate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class RetryConfig {

    @Value("${app.retry.max-attempts:3}")
    private int maxAttempts;

    @Value("${app.retry.initial-interval:1000}")
    private long initialInterval;

    @Value("${app.retry.multiplier:2.0}")
    private double multiplier;

    @Value("${app.retry.max-interval:10000}")
    private long maxInterval;
} 