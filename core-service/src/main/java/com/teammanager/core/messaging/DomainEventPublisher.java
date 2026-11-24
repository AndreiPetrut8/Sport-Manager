package com.teammanager.core.messaging;

import com.teammanager.contracts.events.MatchScheduledEvent;
import com.teammanager.contracts.events.RosterUpdatedEvent;
import com.teammanager.contracts.events.TacticProposedEvent;
import com.teammanager.contracts.messaging.MessagingTopology;
import com.teammanager.core.config.AsyncConcurrencyConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Publishes domain events to RabbitMQ strictly AFTER the database transaction commits,
 * executed on the dedicated bounded domainEventExecutor thread pool.
 */
@Component
public class DomainEventPublisher {

    private static final Logger log = LoggerFactory.getLogger(DomainEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final boolean brokerEnabled;

    public DomainEventPublisher(RabbitTemplate rabbitTemplate,
                                @Value("${app.messaging.broker-enabled:false}") boolean brokerEnabled) {
        this.rabbitTemplate = rabbitTemplate;
        this.brokerEnabled = brokerEnabled;
    }

    @Async(AsyncConcurrencyConfig.DOMAIN_EVENT_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onRosterUpdated(RosterUpdatedEvent event) {
        forwardOrLog(MessagingTopology.ROUTING_ROSTER_UPDATED, event);
    }

    @Async(AsyncConcurrencyConfig.DOMAIN_EVENT_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMatchScheduled(MatchScheduledEvent event) {
        forwardOrLog(MessagingTopology.ROUTING_MATCH_SCHEDULED, event);
    }

    @Async(AsyncConcurrencyConfig.DOMAIN_EVENT_EXECUTOR)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTacticProposed(TacticProposedEvent event) {
        forwardOrLog(MessagingTopology.ROUTING_TACTIC_PROPOSED, event);
    }

    private void forwardOrLog(String routingKey, Object event) {
        if (!brokerEnabled) {
            log.info("[LOCAL-DEV EVENT] routingKey={} thread={} payload={}",
                    routingKey, Thread.currentThread().getName(), event);
            return;
        }
        rabbitTemplate.convertAndSend(MessagingTopology.EVENTS_EXCHANGE, routingKey, event);
        log.info("Published domain event routingKey={} on thread={}", routingKey, Thread.currentThread().getName());
    }
}
