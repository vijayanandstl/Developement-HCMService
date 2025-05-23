package tech.stl.hcm.candidate.config;

import io.micrometer.core.instrument.MeterRegistry;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.retry.annotation.EnableRetry;
import tech.stl.hcm.candidate.model.Candidate;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableRetry
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = false)
public class KafkaConfig {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConfig.class);

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Value("${kafka.topic.candidate-created}")
    private String candidateCreatedTopic;

    @Value("${kafka.topic.candidate-updated}")
    private String candidateUpdatedTopic;

    @Value("${kafka.topic.candidate-deleted}")
    private String candidateDeletedTopic;

    @Value("${kafka.topic.candidate-education-created}")
    private String candidateEducationCreatedTopic;

    private final MeterRegistry meterRegistry;

    public KafkaConfig(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        logger.info("Initializing Kafka configuration with bootstrap servers: {}", bootstrapServers);
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        configs.put(AdminClientConfig.DEFAULT_API_TIMEOUT_MS_CONFIG, 10000);
        configs.put(AdminClientConfig.RETRIES_CONFIG, 3);
        
        KafkaAdmin admin = new KafkaAdmin(configs);
        admin.setFatalIfBrokerNotAvailable(true);
        admin.setAutoCreate(true);
        
        logger.info("KafkaAdmin initialized with bootstrap servers: {}", bootstrapServers);
        return admin;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 5);
        configProps.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 10000);
        configProps.put(ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG, 120000);
        
        logger.info("ProducerFactory initialized with idempotence enabled and acks=all");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ProducerFactory<String, Candidate> candidateProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        
        logger.info("CandidateProducerFactory initialized with idempotence enabled");
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Candidate> candidateKafkaTemplate() {
        return new KafkaTemplate<>(candidateProducerFactory());
    }

    @Bean
    public NewTopic candidateCreatedTopic() {
        return new NewTopic("candidate-created", 3, (short) 1);
    }

    @Bean
    public NewTopic candidateUpdatedTopic() {
        return new NewTopic("candidate-updated", 3, (short) 1);
    }

    @Bean
    public NewTopic candidateDeletedTopic() {
        return new NewTopic("candidate-deleted", 3, (short) 1);
    }

    @Bean
    public NewTopic candidateEducationCreatedTopic() {
        return new NewTopic(candidateEducationCreatedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic candidateWorkHistoryCreatedTopic() {
        return new NewTopic("candidate-work-history-created", 1, (short) 1);
    }

    @Bean
    public NewTopic candidateCertificationCreatedTopic() {
        return new NewTopic("candidate-certification-created", 1, (short) 1);
    }

    @Bean
    public NewTopic skillCreatedTopic() {
        return new NewTopic("skill-created", 1, (short) 1);
    }

    @Bean
    public NewTopic candidateSkillCreatedTopic() {
        return new NewTopic("candidate-skill-created", 1, (short) 1);
    }
} 