package tech.stl.hcm.candidate.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class KafkaConnectionTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Test
    public void testKafkaConnection() throws Exception {
        // Test message
        String testMessage = "Test Kafka Connection";
        String topic = "candidate-created"; // Using actual topic from application

        // Send message
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, testMessage);
        SendResult<String, Object> result = future.get(10, TimeUnit.SECONDS);

        // Verify
        assertNotNull(result);
        assertNotNull(result.getRecordMetadata());
        assertEquals(topic, result.getRecordMetadata().topic());
        
        // Log success
        System.out.println("Successfully sent message to Kafka: " + testMessage);
        System.out.println("Topic: " + result.getRecordMetadata().topic());
        System.out.println("Partition: " + result.getRecordMetadata().partition());
        System.out.println("Offset: " + result.getRecordMetadata().offset());
    }
} 