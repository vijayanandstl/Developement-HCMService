package tech.stl.hcm.candidate.dto;

import java.util.UUID;

public class SkillDTO {
    private UUID skillId;
    private String skillName;
    private String skillCategory;

    // Getters and setters
    public UUID getSkillId() {
        return skillId;
    }

    public void setSkillId(UUID skillId) {
        this.skillId = skillId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillCategory() {
        return skillCategory;
    }

    public void setSkillCategory(String skillCategory) {
        this.skillCategory = skillCategory;
    }
} 