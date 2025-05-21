package tech.stl.hcm.candidate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableKafka
@EnableRetry
@ComponentScan(basePackages = "tech.stl.hcm.candidate")
@EntityScan("tech.stl.hcm.candidate.model")
@EnableJpaRepositories("tech.stl.hcm.candidate.repository")
public class CandidateMicroserviceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CandidateMicroserviceApplication.class, args);
    }
} 