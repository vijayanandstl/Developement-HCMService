package tech.stl.hcm.candidate.service;

import tech.stl.hcm.candidate.model.Candidate;
import tech.stl.hcm.candidate.repository.CandidateRepository;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tech.stl.hcm.candidate.dto.CandidateDTO;
import tech.stl.hcm.candidate.exception.CandidateNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Date;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final KafkaTemplate<String, Candidate> kafkaTemplate;
    private static final UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID DEFAULT_ORGANIZATION_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID DEFAULT_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, 
                          @Autowired(required = false) KafkaTemplate<String, Candidate> kafkaTemplate) {
        this.candidateRepository = candidateRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<CandidateDTO> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(candidate -> {
                    CandidateDTO dto = new CandidateDTO();
                    BeanUtils.copyProperties(candidate, dto);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
        Candidate candidate = new Candidate();
        BeanUtils.copyProperties(candidateDTO, candidate);
        
        // Set required fields
        Date now = new Date();
        candidate.setCreatedAt(now);
        candidate.setUpdatedAt(now);
        
        // Set default values for required fields if not provided
        if (candidate.getTenantId() == null) {
            candidate.setTenantId(DEFAULT_TENANT_ID);
        }
        if (candidate.getOrganizationId() == null) {
            candidate.setOrganizationId(DEFAULT_ORGANIZATION_ID);
        }
        if (candidate.getCreatedBy() == null) {
            candidate.setCreatedBy(DEFAULT_USER_ID);
        }
        if (candidate.getUpdatedBy() == null) {
            candidate.setUpdatedBy(candidate.getCreatedBy());
        }

        Candidate saved = candidateRepository.save(candidate);
        if (kafkaTemplate != null) {
            try {
                kafkaTemplate.send("candidate.create", saved);
            } catch (Exception e) {
                // Log error but don't fail the request
                System.err.println("Failed to send Kafka message: " + e.getMessage());
            }
        }
        
        CandidateDTO result = new CandidateDTO();
        BeanUtils.copyProperties(saved, result);
        return result;
    }

    public Optional<CandidateDTO> getCandidate(UUID id) {
        return candidateRepository.findById(id).map(candidate -> {
            CandidateDTO dto = new CandidateDTO();
            BeanUtils.copyProperties(candidate, dto);
            return dto;
        });
    }

    public CandidateDTO updateCandidate(CandidateDTO candidateDTO) {
        Candidate candidate = candidateRepository.findById(candidateDTO.getCandidateId())
                .orElseThrow(() -> new CandidateNotFoundException("Candidate not found: " + candidateDTO.getCandidateId()));
        BeanUtils.copyProperties(candidateDTO, candidate);
        
        // Update audit fields
        candidate.setUpdatedAt(new Date());
        if (candidate.getUpdatedBy() == null) {
            candidate.setUpdatedBy(DEFAULT_USER_ID);
        }
        
        Candidate updated = candidateRepository.save(candidate);
        if (kafkaTemplate != null) {
            try {
                kafkaTemplate.send("candidate.update", updated);
            } catch (Exception e) {
                // Log error but don't fail the request
                System.err.println("Failed to send Kafka message: " + e.getMessage());
            }
        }
        
        CandidateDTO result = new CandidateDTO();
        BeanUtils.copyProperties(updated, result);
        return result;
    }

    public void deleteCandidate(UUID id) {
        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new CandidateNotFoundException("Candidate not found: " + id));
        candidateRepository.deleteById(id);
        if (kafkaTemplate != null) {
            try {
                kafkaTemplate.send("candidate.delete", candidate);
            } catch (Exception e) {
                // Log error but don't fail the request
                System.err.println("Failed to send Kafka message: " + e.getMessage());
            }
        }
    }
} 