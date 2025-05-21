package tech.stl.hcm.candidate.service;

import tech.stl.hcm.candidate.dto.CandidateEducationDTO;
import tech.stl.hcm.candidate.model.Candidate;
import tech.stl.hcm.candidate.model.CandidateEducation;
import tech.stl.hcm.candidate.repository.CandidateEducationRepository;
import tech.stl.hcm.candidate.repository.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.retry.annotation.Retryable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CandidateEducationService {
    private static final Logger logger = LoggerFactory.getLogger(CandidateEducationService.class);

    @Autowired
    private CandidateEducationRepository educationRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Retryable(value = OptimisticLockingFailureException.class, maxAttempts = 3)
    public CandidateEducationDTO createEducation(CandidateEducationDTO educationDTO) {
        Candidate candidate = candidateRepository.findById(educationDTO.getCandidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        CandidateEducation education = new CandidateEducation();
        education.setCandidate(candidate);
        education.setInstitution(educationDTO.getInstitution());
        education.setDegree(educationDTO.getDegree());
        education.setFieldOfStudy(educationDTO.getFieldOfStudy());
        education.setStartDate(educationDTO.getStartDate());
        education.setEndDate(educationDTO.getEndDate());
        education.setGrade(educationDTO.getGrade());
        education.setNotes(educationDTO.getNotes());

        CandidateEducation savedEducation = educationRepository.save(education);
        educationDTO.setEducationId(savedEducation.getEducationId());

        // Send to Kafka topic with retry mechanism
        try {
            kafkaTemplate.send("candidate-education-created", educationDTO)
                    .get(); // Wait for the send to complete
            logger.info("Successfully sent education record to Kafka for candidate: {}", candidate.getCandidateId());
        } catch (Exception e) {
            logger.error("Failed to send message to Kafka for candidate: {}", candidate.getCandidateId(), e);
            // Don't throw the exception, just log it
        }

        return educationDTO;
    }

    @Transactional(readOnly = true)
    public List<CandidateEducationDTO> getEducationByCandidateId(UUID candidateId) {
        return educationRepository.findByCandidate_CandidateId(candidateId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CandidateEducationDTO convertToDTO(CandidateEducation education) {
        CandidateEducationDTO dto = new CandidateEducationDTO();
        dto.setEducationId(education.getEducationId());
        dto.setCandidateId(education.getCandidate().getCandidateId());
        dto.setInstitution(education.getInstitution());
        dto.setDegree(education.getDegree());
        dto.setFieldOfStudy(education.getFieldOfStudy());
        dto.setStartDate(education.getStartDate());
        dto.setEndDate(education.getEndDate());
        dto.setGrade(education.getGrade());
        dto.setNotes(education.getNotes());
        return dto;
    }

    public CandidateEducation saveEducation(CandidateEducation education) {
        return educationRepository.save(education);
    }

    public void deleteEducation(Integer educationId) {
        educationRepository.deleteById(educationId);
    }

    public CandidateEducation getEducationById(Integer educationId) {
        return educationRepository.findById(educationId)
                .orElseThrow(() -> new RuntimeException("Education not found with id: " + educationId));
    }
} 