package tech.stl.hcm.candidate.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class KafkaConsumerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    private CountDownLatch latch = new CountDownLatch(1);
    private String receivedMessage;

    @KafkaListener(topics = "candidate-created", groupId = "hcm-candidate-service-group")
    public void listen(@Payload String message,
                      @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                      @Header(KafkaHeaders.RECEIVED_PARTITION) int partition,
                      @Header(KafkaHeaders.OFFSET) long offset) {
        System.out.println("Received message: " + message);
        System.out.println("From topic: " + topic);
        System.out.println("Partition: " + partition);
        System.out.println("Offset: " + offset);
        receivedMessage = message;
        latch.countDown();
    }

    @Test
    public void testKafkaConsumer() throws Exception {
        // Test message
        String testMessage = "Test Kafka Consumer";
        String topic = "candidate-created";

        // Send message
        kafkaTemplate.send(topic, testMessage);

        // Wait for message to be received
        boolean messageReceived = latch.await(10, TimeUnit.SECONDS);

        // Verify
        assertTrue(messageReceived, "Message was not received");
        assertEquals(testMessage, receivedMessage, "Received message does not match sent message");
        
        System.out.println("Successfully received message from Kafka: " + receivedMessage);
    }
} 