/// --- FILE: src/main/resources/application.properties

# Kafka bootstrap server
spring.kafka.bootstrap-servers=192.168.0.155:30092

# Kafka Producer
spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=org.apache.kafka.common.serialization.StringSerializer

# Kafka Consumer
spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.value-deserializer=org.apache.kafka.common.serialization.StringDeserializer
spring.kafka.consumer.group-id=candidate-group
spring.kafka.consumer.auto-offset-reset=earliest

# Admin client - disable auto topic creation
spring.kafka.admin.auto-create=false

kafka.topic.default=candidate-created

/// --- FILE: src/main/java/com/example/kafka/KafkaApplication.java

package com.example.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class KafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }
}

/// --- FILE: src/main/java/com/example/kafka/config/KafkaTopics.java

package com.example.kafka.config;

public class KafkaTopics {
    public static final String CANDIDATE_CREATED = "candidate-created";
    public static final String CANDIDATE_UPDATED = "candidate-updated";
    public static final String CANDIDATE_DELETED = "candidate-deleted";
    public static final String CANDIDATE_EDUCATION_CREATED = "candidate-education-created";
    public static final String CANDIDATE_WORK_HISTORY_CREATED = "candidate-work-history-created";
    public static final String CANDIDATE_CERTIFICATION_CREATED = "candidate-certification-created";
    public static final String SKILL_CREATED = "skill-created";
    public static final String CANDIDATE_SKILL_CREATED = "candidate-skill-created";
}

/// --- FILE: src/main/java/com/example/kafka/service/KafkaProducerService.java

package com.example.kafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}

/// --- FILE: src/main/java/com/example/kafka/listener/KafkaConsumerListener.java

package com.example.kafka.listener;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerListener {

    @KafkaListener(topics = {
            "candidate-created",
            "candidate-updated",
            "candidate-deleted",
            "candidate-education-created",
            "candidate-work-history-created",
            "candidate-certification-created",
            "skill-created",
            "candidate-skill-created"
    }, groupId = "candidate-group")
    public void consume(String message) {
        System.out.println("Received message: " + message);
    }
}

/// --- FILE: src/main/java/com/example/kafka/controller/KafkaTestController.java

package com.example.kafka.controller;

import com.example.kafka.config.KafkaTopics;
import com.example.kafka.service.KafkaProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kafka")
public class KafkaTestController {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    @PostMapping("/send/{topic}")
    public String sendToTopic(@PathVariable String topic, @RequestBody String message) {
        kafkaProducerService.sendMessage(topic, message);
        return "Message sent to topic: " + topic;
    }

    // Quick test endpoint for default topic
    @PostMapping("/send")
    public String sendDefault(@RequestBody String message) {
        kafkaProducerService.sendMessage(KafkaTopics.CANDIDATE_CREATED, message);
        return "Message sent to default topic: " + KafkaTopics.CANDIDATE_CREATED;
    }
}
