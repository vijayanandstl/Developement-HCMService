package tech.stl.hcm.candidate.dto;

import java.util.UUID;

public class CandidateSkillDTO {
    private UUID candidateSkillId;
    private UUID candidateId;
    private UUID skillId;
    private String proficiencyLevel;

    // Getters and setters
    public UUID getCandidateSkillId() {
        return candidateSkillId;
    }

    public void setCandidateSkillId(UUID candidateSkillId) {
        this.candidateSkillId = candidateSkillId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public UUID getSkillId() {
        return skillId;
    }

    public void setSkillId(UUID skillId) {
        this.skillId = skillId;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }
} 