package tech.stl.hcm.candidate.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimitConfig {

    @Value("${app.rate-limit.enabled:true}")
    private boolean rateLimitEnabled;

    @Value("${app.rate-limit.capacity:100}")
    private long capacity;

    @Value("${app.rate-limit.refill-tokens:100}")
    private long refillTokens;

    @Value("${app.rate-limit.refill-duration:60}")
    private long refillDuration;

    @Bean
    public Bucket createNewBucket() {
        if (!rateLimitEnabled) {
            return Bucket4j.builder()
                    .addLimit(Bandwidth.simple(Long.MAX_VALUE, Duration.ofSeconds(1)))
                    .build();
        }

        Refill refill = Refill.intervally(refillTokens, Duration.ofSeconds(refillDuration));
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        
        return Bucket4j.builder()
                .addLimit(limit)
                .build();
    }
} 