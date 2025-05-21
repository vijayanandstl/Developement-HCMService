package tech.stl.hcm.candidate.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"candidate-created", "candidate-updated", "candidate-deleted"})
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
    "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
    "spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer",
    "spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer",
    "spring.kafka.consumer.auto-offset-reset=earliest",
    "spring.kafka.consumer.group-id=test-group"
})
public class KafkaConsumerTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private CountDownLatch latch = new CountDownLatch(1);
    private String receivedMessage;

    @KafkaListener(topics = "candidate-created", groupId = "test-group")
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