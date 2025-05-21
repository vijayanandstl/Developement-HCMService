package tech.stl.hcm.candidate.model;

import java.io.Serializable;
import java.util.UUID;

public class CandidateSkillId implements Serializable {
    private UUID candidate;
    private Integer skill;

    public CandidateSkillId() {
    }

    public CandidateSkillId(UUID candidate, Integer skill) {
        this.candidate = candidate;
        this.skill = skill;
    }

    public UUID getCandidate() {
        return candidate;
    }

    public void setCandidate(UUID candidate) {
        this.candidate = candidate;
    }

    public Integer getSkill() {
        return skill;
    }

    public void setSkill(Integer skill) {
        this.skill = skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CandidateSkillId that = (CandidateSkillId) o;

        if (!candidate.equals(that.candidate)) return false;
        return skill.equals(that.skill);
    }

    @Override
    public int hashCode() {
        int result = candidate.hashCode();
        result = 31 * result + skill.hashCode();
        return result;
    }
} 