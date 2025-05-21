package tech.stl.hcm.candidate.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class AuditConfig {

    @Bean
    public AuditorAware<UUID> auditorProvider() {
        // For now, we'll use a fixed UUID. In a real application, this would come from the security context
        return () -> Optional.of(UUID.fromString("00000000-0000-0000-0000-000000000000"));
    }
} 