package tech.stl.hcm.candidate.repository;

import tech.stl.hcm.candidate.model.CandidateCertification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CandidateCertificationRepository extends JpaRepository<CandidateCertification, UUID> {
    List<CandidateCertification> findByCandidate_CandidateId(UUID candidateId);
} 