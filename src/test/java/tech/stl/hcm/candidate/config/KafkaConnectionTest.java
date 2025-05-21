package tech.stl.hcm.candidate.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.kafka.support.SendResult;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"test-topic"})
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
    "spring.kafka.producer.key-serializer=org.apache.kafka.common.serialization.StringSerializer",
    "spring.kafka.producer.value-serializer=org.springframework.kafka.support.serializer.JsonSerializer",
    "spring.kafka.consumer.key-deserializer=org.apache.kafka.common.serialization.StringDeserializer",
    "spring.kafka.consumer.value-deserializer=org.springframework.kafka.support.serializer.JsonDeserializer",
    "spring.kafka.consumer.auto-offset-reset=earliest"
})
public class KafkaConnectionTest {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Test
    public void testKafkaConnection() throws Exception {
        // Test message
        String testMessage = "Test Kafka Connection";
        String topic = "test-topic";

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