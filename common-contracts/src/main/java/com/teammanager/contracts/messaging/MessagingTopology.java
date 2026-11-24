package com.teammanager.contracts.messaging;

/**
 * Shared RabbitMQ exchange, routing keys, and queue names across distributed components.
 */
public final class MessagingTopology {

    public static final String EVENTS_EXCHANGE = "teammanager.domain.events";

    public static final String ROUTING_ROSTER_UPDATED = "roster.updated";
    public static final String ROUTING_MATCH_SCHEDULED = "match.scheduled";
    public static final String ROUTING_TACTIC_PROPOSED = "tactic.proposed";

    public static final String QUEUE_REALTIME_EVENTS = "realtime.domain-events.q";
    public static final String QUEUE_AGENT_TRIGGERS = "agent.triggers.q";

    private MessagingTopology() {
    }
}
