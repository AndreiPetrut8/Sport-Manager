package com.teammanager.mcp.tools;

import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Declares the remote MCP tools exposed by mcp-server (>= 3 tools: 2 read tools + 1 propose-change tool).
 * JSON-RPC / MCP protocol wiring will bind to these definitions in the next implementation phase.
 */
@Component
public class McpToolCatalog {

    public List<ToolDescriptor> registeredTools() {
        return List.of(
                new ToolDescriptor(
                        "get_anonymized_squad_metrics",
                        "READ",
                        "Returns anonymized player tokens, positions, availability, and sport-specific numeric metrics."
                ),
                new ToolDescriptor(
                        "get_public_opponent_stats",
                        "READ",
                        "Returns public aggregate performance metrics for a scheduled opponent."
                ),
                new ToolDescriptor(
                        "propose_tactical_plan",
                        "PROPOSE_CHANGE",
                        "Submits a PENDING tactical formation and anonymized starter lineup to core-service for manager approval."
                )
        );
    }

    public record ToolDescriptor(String name, String category, String description) {
    }
}
