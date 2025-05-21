package tech.stl.hcm.candidate.repository;

import tech.stl.hcm.candidate.model.CandidateWorkHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CandidateWorkHistoryRepository extends JpaRepository<CandidateWorkHistory, UUID> {
    List<CandidateWorkHistory> findByCandidate_CandidateId(UUID candidateId);
} 