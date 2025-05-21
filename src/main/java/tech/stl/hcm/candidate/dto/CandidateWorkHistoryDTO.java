package tech.stl.hcm.candidate.dto;

import java.util.Date;
import java.util.UUID;

public class CandidateWorkHistoryDTO {
    private UUID workHistoryId;
    private UUID candidateId;
    private String company;
    private String position;
    private Date startDate;
    private Date endDate;
    private String description;

    // Getters and setters
    public UUID getWorkHistoryId() {
        return workHistoryId;
    }

    public void setWorkHistoryId(UUID workHistoryId) {
        this.workHistoryId = workHistoryId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
} 