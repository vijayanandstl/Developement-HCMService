package tech.stl.hcm.candidate.config;

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

    @Value("${kafka.topic.candidate-created}")
    private String candidateCreatedTopic;

    @Value("${kafka.topic.candidate-updated}")
    private String candidateUpdatedTopic;

    @Value("${kafka.topic.candidate-deleted}")
    private String candidateDeletedTopic;

    @Value("${kafka.topic.candidate-education-created}")
    private String candidateEducationCreatedTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic candidateCreatedTopic() {
        return new NewTopic(candidateCreatedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic candidateUpdatedTopic() {
        return new NewTopic(candidateUpdatedTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic candidateDeletedTopic() {
        return new NewTopic(candidateDeletedTopic, 1, (short) 1);
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

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
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
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, Candidate> candidateKafkaTemplate() {
        return new KafkaTemplate<>(candidateProducerFactory());
    }
} 