package tech.stl.hcm.candidate.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tech.stl.hcm.candidate.dto.CandidateDTO;
import tech.stl.hcm.candidate.model.Candidate;
import tech.stl.hcm.candidate.service.CandidateService;
import java.util.UUID;

@Component
public class KafkaConsumerListener {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerListener.class);
    private static final String CONSUMER_GROUP = "hcm-candidate-service-group";

    @Autowired
    private CandidateService candidateService;

    @KafkaListener(topics = "${kafka.topic.candidate-created}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenCandidateCreated(Candidate candidate) {
        try {
            logger.info("Received candidate creation message: {}", candidate);
            // Process the candidate
            candidateService.processCandidate(candidate);
        } catch (Exception e) {
            logger.error("Error processing candidate creation message: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${kafka.topic.candidate-updated}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenCandidateUpdated(Candidate candidate) {
        try {
            logger.info("Received candidate update message: {}", candidate);
            CandidateDTO dto = new CandidateDTO();
            org.springframework.beans.BeanUtils.copyProperties(candidate, dto);
            candidateService.updateCandidate(dto);
            logger.info("Successfully processed candidate update message");
        } catch (Exception e) {
            logger.error("Error processing candidate update message: {}", e.getMessage(), e);
        }
    }

    @KafkaListener(topics = "${kafka.topic.candidate-deleted}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenCandidateDeleted(String message) {
        try {
            logger.info("Received candidate deletion message: {}", message);
            UUID candidateId = UUID.fromString(message);
            candidateService.deleteCandidate(candidateId);
            logger.info("Successfully processed candidate deletion message");
        } catch (Exception e) {
            logger.error("Error processing candidate deletion message: {}", e.getMessage(), e);
        }
    }
} 