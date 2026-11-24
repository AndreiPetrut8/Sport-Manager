package com.teammanager.contracts.events;

import com.teammanager.contracts.domain.SportCode;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable event published when a new match is scheduled (triggers tactical-agent preparation).
 */
public record MatchScheduledEvent(
        UUID eventId,
        Long matchId,
        Long teamId,
        SportCode sportCode,
        String opponentName,
        Instant scheduledAt,
        Instant occurredAt
) {
}
