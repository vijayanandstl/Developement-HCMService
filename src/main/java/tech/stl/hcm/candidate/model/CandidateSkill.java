package tech.stl.hcm.candidate.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "candidate_skill", schema = "hcm")
@IdClass(CandidateSkillId.class)
public class CandidateSkill {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(name = "proficiency_level", length = 50)
    private String proficiencyLevel;

    @Column(name = "years_of_experience")
    private Integer yearsOfExperience;

    @Column(name = "created_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "updated_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @Column(name = "updated_by", nullable = false)
    private UUID updatedBy;

    // Getters and setters
    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }

    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }
} 