package tech.stl.hcm.candidate.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class MetricsService {

    private final Counter candidateCreatedCounter;
    private final Counter educationCreatedCounter;
    private final Counter workHistoryCreatedCounter;
    private final Counter certificationCreatedCounter;

    public MetricsService(MeterRegistry registry) {
        this.candidateCreatedCounter = Counter.builder("candidate.created")
                .description("Number of candidates created")
                .register(registry);
        this.educationCreatedCounter = Counter.builder("education.created")
                .description("Number of education records created")
                .register(registry);
        this.workHistoryCreatedCounter = Counter.builder("work_history.created")
                .description("Number of work history records created")
                .register(registry);
        this.certificationCreatedCounter = Counter.builder("certification.created")
                .description("Number of certifications created")
                .register(registry);
    }

    public void incrementCandidateCreated() {
        candidateCreatedCounter.increment();
    }

    public void incrementEducationCreated() {
        educationCreatedCounter.increment();
    }

    public void incrementWorkHistoryCreated() {
        workHistoryCreatedCounter.increment();
    }

    public void incrementCertificationCreated() {
        certificationCreatedCounter.increment();
    }
} 