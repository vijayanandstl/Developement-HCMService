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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Date;

@Service
public class CandidateService {
    private final CandidateRepository candidateRepository;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final UUID DEFAULT_TENANT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID DEFAULT_ORGANIZATION_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID DEFAULT_USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    private static final Logger logger = LoggerFactory.getLogger(CandidateService.class);

    @Autowired
    public CandidateService(CandidateRepository candidateRepository, 
                          @Autowired(required = false) KafkaTemplate<String, Object> kafkaTemplate) {
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
        
        // Send Kafka message with retry and error handling
        if (kafkaTemplate != null) {
            try {
                logger.info("Sending candidate creation message to Kafka for candidate ID: {}", saved.getCandidateId());
                kafkaTemplate.send("candidate-created", saved)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            logger.info("Successfully sent candidate creation message to Kafka. Topic: {}, Partition: {}, Offset: {}", 
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                        } else {
                            logger.error("Failed to send candidate creation message to Kafka for candidate ID: {}", 
                                saved.getCandidateId(), ex);
                        }
                    });
            } catch (Exception e) {
                logger.error("Error while sending candidate creation message to Kafka for candidate ID: {}", 
                    saved.getCandidateId(), e);
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
        
        // Send Kafka message with retry and error handling
        if (kafkaTemplate != null) {
            try {
                logger.info("Sending candidate update message to Kafka for candidate ID: {}", updated.getCandidateId());
                kafkaTemplate.send("candidate-updated", updated)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            logger.info("Successfully sent candidate update message to Kafka. Topic: {}, Partition: {}, Offset: {}", 
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                        } else {
                            logger.error("Failed to send candidate update message to Kafka for candidate ID: {}", 
                                updated.getCandidateId(), ex);
                        }
                    });
            } catch (Exception e) {
                logger.error("Error while sending candidate update message to Kafka for candidate ID: {}", 
                    updated.getCandidateId(), e);
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
        
        // Send Kafka message with retry and error handling
        if (kafkaTemplate != null) {
            try {
                logger.info("Sending candidate deletion message to Kafka for candidate ID: {}", id);
                kafkaTemplate.send("candidate-deleted", candidate)
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            logger.info("Successfully sent candidate deletion message to Kafka. Topic: {}, Partition: {}, Offset: {}", 
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                        } else {
                            logger.error("Failed to send candidate deletion message to Kafka for candidate ID: {}", 
                                id, ex);
                        }
                    });
            } catch (Exception e) {
                logger.error("Error while sending candidate deletion message to Kafka for candidate ID: {}", 
                    id, e);
            }
        }
    }

    public void processCandidate(Candidate candidate) {
        try {
            logger.info("Processing candidate from Kafka message: {}", candidate);
            CandidateDTO dto = new CandidateDTO();
            BeanUtils.copyProperties(candidate, dto);
            createCandidate(dto);
            logger.info("Successfully processed candidate from Kafka message");
        } catch (Exception e) {
            logger.error("Error processing candidate from Kafka message: {}", e.getMessage(), e);
            throw e;
        }
    }
} 