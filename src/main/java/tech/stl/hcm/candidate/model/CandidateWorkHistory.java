package tech.stl.hcm.candidate.model;

import jakarta.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "candidate_work_history", schema = "hcm")
public class CandidateWorkHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "work_history_id", columnDefinition = "SERIAL")
    private Integer workHistoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @Column(name = "company_name", length = 200)
    private String companyName;

    @Column(name = "position_title", length = 100)
    private String positionTitle;

    @Column(name = "location", length = 200)
    private String location;

    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @Column(name = "responsibilities", columnDefinition = "TEXT")
    private String responsibilities;

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
    public Integer getWorkHistoryId() {
        return workHistoryId;
    }

    public void setWorkHistoryId(Integer workHistoryId) {
        this.workHistoryId = workHistoryId;
    }

    public Candidate getCandidate() {
        return candidate;
    }

    public void setCandidate(Candidate candidate) {
        this.candidate = candidate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getResponsibilities() {
        return responsibilities;
    }

    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
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