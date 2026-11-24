package com.teammanager.core.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * Bounded, named thread pool for post-commit domain event publishing (@TransactionalEventListener + @Async).
 * Keeps HTTP worker threads free from broker I/O latency while enforcing backpressure via bounded queue.
 */
@Configuration
@EnableAsync
public class AsyncConcurrencyConfig {

    public static final String DOMAIN_EVENT_EXECUTOR = "domainEventExecutor";

    @Bean(name = DOMAIN_EVENT_EXECUTOR)
    public Executor domainEventExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("core-event-pub-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(10);
        executor.initialize();
        return executor;
    }
}
