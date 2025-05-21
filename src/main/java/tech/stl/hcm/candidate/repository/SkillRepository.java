package tech.stl.hcm.candidate.repository;

import tech.stl.hcm.candidate.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface SkillRepository extends JpaRepository<Skill, UUID> {
    Skill findBySkillName(String skillName);
} 