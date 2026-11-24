package com.teammanager.contracts.events;

import com.teammanager.contracts.domain.ProposalStatus;
import java.time.Instant;
import java.util.UUID;

/**
 * Immutable event published when a new tactical plan is proposed (e.g. by tactical-agent via MCP).
 */
public record TacticProposedEvent(
        UUID eventId,
        Long proposalId,
        Long matchId,
        Long teamId,
        String formation,
        ProposalStatus status,
        String proposedBy,
        Instant occurredAt
) {
}
