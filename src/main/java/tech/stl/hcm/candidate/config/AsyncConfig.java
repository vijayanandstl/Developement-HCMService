package tech.stl.hcm.candidate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Value("${app.thread-pool.core-size:10}")
    private int corePoolSize;

    @Value("${app.thread-pool.max-size:20}")
    private int maxPoolSize;

    @Value("${app.thread-pool.queue-capacity:500}")
    private int queueCapacity;

    @Value("${app.thread-pool.keep-alive-seconds:60}")
    private int keepAliveSeconds;

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setThreadNamePrefix("CandidateAsync-");
        executor.initialize();
        return executor;
    }
} 