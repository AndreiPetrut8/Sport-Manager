package com.teammanager.contracts.events;

import com.teammanager.contracts.domain.PlayerStatus;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable event published by core-service after a roster or player availability change commits.
 */
public record RosterUpdatedEvent(
        UUID eventId,
        Long teamId,
        String teamName,
        Long playerId,
        PlayerStatus playerStatus,
        String summary,
        Instant occurredAt
) {
}
