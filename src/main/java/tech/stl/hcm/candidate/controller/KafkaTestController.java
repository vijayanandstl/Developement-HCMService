package tech.stl.hcm.candidate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import tech.stl.hcm.candidate.dto.CandidateDTO;

@RestController
@RequestMapping("/api/kafka-test")
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true")
public class KafkaTestController {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @PostMapping("/candidate/create")
    public String createCandidate(@RequestBody CandidateDTO candidate) {
        String topic = "candidate-created";
        kafkaTemplate.send(topic, candidate);
        return "Message sent to topic: " + topic;
    }

    @PutMapping("/candidate/update")
    public String updateCandidate(@RequestBody CandidateDTO candidate) {
        String topic = "candidate-updated";
        kafkaTemplate.send(topic, candidate);
        return "Message sent to topic: " + topic;
    }

    @DeleteMapping("/candidate/delete/{id}")
    public String deleteCandidate(@PathVariable Long id) {
        String topic = "candidate-deleted";
        kafkaTemplate.send(topic, id);
        return "Message sent to topic: " + topic;
    }

    @GetMapping("/health")
    public String health() {
        return "Kafka test controller is running";
    }
} 