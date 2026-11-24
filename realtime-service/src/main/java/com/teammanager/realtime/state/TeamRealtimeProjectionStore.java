package com.teammanager.realtime.state;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.springframework.stereotype.Component;

/**
 * Thread-safe in-memory read projection built exclusively from RabbitMQ domain events
 * (respecting the Single-Source-of-Truth rule: realtime-service never touches core-db).
 * Concurrent RabbitMQ listener threads write alerts while WebSocket/HTTP threads read lock-free snapshots.
 */
@Component
public class TeamRealtimeProjectionStore {

    private static final int MAX_RECENT_ALERTS_PER_TEAM = 50;

    private final ConcurrentHashMap<Long, ConcurrentLinkedDeque<TeamAlertSnapshot>> alertsByTeam =
            new ConcurrentHashMap<>();

    public void appendAlert(Long teamId, String type, String message, Instant occurredAt) {
        ConcurrentLinkedDeque<TeamAlertSnapshot> deque =
                alertsByTeam.computeIfAbsent(teamId, ignored -> new ConcurrentLinkedDeque<>());
        deque.addFirst(new TeamAlertSnapshot(type, message, occurredAt));
        while (deque.size() > MAX_RECENT_ALERTS_PER_TEAM) {
            deque.pollLast();
        }
    }

    public List<TeamAlertSnapshot> recentAlerts(Long teamId) {
        ConcurrentLinkedDeque<TeamAlertSnapshot> deque = alertsByTeam.get(teamId);
        return deque == null ? List.of() : List.copyOf(deque);
    }

    public record TeamAlertSnapshot(String type, String message, Instant occurredAt) {
    }
}
