package tech.stl.hcm.candidate.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class CandidateCircuitBreakerConfig {

    @Value("${app.circuit-breaker.enabled:true}")
    private boolean circuitBreakerEnabled;

    @Value("${app.circuit-breaker.sliding-window-size:10}")
    private int slidingWindowSize;

    @Value("${app.circuit-breaker.failure-rate-threshold:50}")
    private float failureRateThreshold;

    @Value("${app.circuit-breaker.wait-duration-in-open-state:60}")
    private int waitDurationInOpenState;

    @Value("${app.circuit-breaker.permitted-number-of-calls-in-half-open-state:5}")
    private int permittedNumberOfCallsInHalfOpenState;

    @Bean
    public CircuitBreaker candidateServiceCircuitBreaker() {
        if (!circuitBreakerEnabled) {
            return CircuitBreaker.of("candidateService", CircuitBreakerConfig.custom()
                    .slidingWindowType(SlidingWindowType.COUNT_BASED)
                    .slidingWindowSize(1000)
                    .failureRateThreshold(100)
                    .build());
        }

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .slidingWindowType(SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(slidingWindowSize)
                .failureRateThreshold(failureRateThreshold)
                .waitDurationInOpenState(Duration.ofSeconds(waitDurationInOpenState))
                .permittedNumberOfCallsInHalfOpenState(permittedNumberOfCallsInHalfOpenState)
                .build();

        return CircuitBreaker.of("candidateService", circuitBreakerConfig);
    }
} 