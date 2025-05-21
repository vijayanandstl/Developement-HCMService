package tech.stl.hcm.candidate.repository;

import tech.stl.hcm.candidate.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CandidateRepository extends JpaRepository<Candidate, UUID> {
} 