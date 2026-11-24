package com.teammanager.mcp.anonymizer;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.stereotype.Component;

/**
 * Thread-safe bidirectional token vault ensuring real player names/IDs never leak to external LLMs,
 * while still allowing MCP write/propose tools (propose_tactical_plan) to resolve pseudonyms back to IDs.
 */
@Component
public class PlayerAnonymizationVault {

    private final AtomicInteger sequence = new AtomicInteger(100);
    private final ConcurrentHashMap<Long, String> idToAlias = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Long> aliasToId = new ConcurrentHashMap<>();

    public String aliasForPlayer(Long realPlayerId) {
        return idToAlias.computeIfAbsent(realPlayerId, id -> {
            String alias = "ATHLETE_" + sequence.incrementAndGet();
            aliasToId.put(alias, id);
            return alias;
        });
    }

    public Optional<Long> resolveRealPlayerId(String alias) {
        return Optional.ofNullable(aliasToId.get(alias));
    }
}
