package tech.stl.hcm.candidate.repository;

import tech.stl.hcm.candidate.model.CandidateSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CandidateSkillRepository extends JpaRepository<CandidateSkill, UUID> {
    List<CandidateSkill> findByCandidate_CandidateId(UUID candidateId);
} 