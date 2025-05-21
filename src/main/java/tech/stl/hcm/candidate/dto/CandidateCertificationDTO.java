package tech.stl.hcm.candidate.dto;

import java.util.Date;
import java.util.UUID;

public class CandidateCertificationDTO {
    private UUID certificationId;
    private UUID candidateId;
    private String certificationName;
    private String issuingOrganization;
    private Date issueDate;
    private Date expiryDate;
    private String credentialId;

    // Getters and setters
    public UUID getCertificationId() {
        return certificationId;
    }

    public void setCertificationId(UUID certificationId) {
        this.certificationId = certificationId;
    }

    public UUID getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(UUID candidateId) {
        this.candidateId = candidateId;
    }

    public String getCertificationName() {
        return certificationName;
    }

    public void setCertificationName(String certificationName) {
        this.certificationName = certificationName;
    }

    public String getIssuingOrganization() {
        return issuingOrganization;
    }

    public void setIssuingOrganization(String issuingOrganization) {
        this.issuingOrganization = issuingOrganization;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }
} 