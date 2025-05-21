package tech.stl.hcm.candidate.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.stl.hcm.candidate.model.CandidateEducation;
import java.util.List;
import java.util.UUID;

public interface CandidateEducationRepository extends JpaRepository<CandidateEducation, Integer> {
    List<CandidateEducation> findByCandidate_CandidateId(UUID candidateId);
} 