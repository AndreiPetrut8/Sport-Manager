package com.teammanager.agent.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/agent")
@Tag(name = "Tactical Agent", description = "Autonomous match-preparation agent status & diagnostics")
public class AgentStatusController {

    private final String mcpBaseUrl;

    public AgentStatusController(@Value("${app.mcp.base-url}") String mcpBaseUrl) {
        this.mcpBaseUrl = mcpBaseUrl;
    }

    @GetMapping("/status")
    @Operation(summary = "Inspect autonomous tactical agent status and configured MCP gateway")
    public Map<String, Object> status() {
        return Map.of(
                "service", "tactical-agent",
                "mcpGateway", mcpBaseUrl,
                "mode", "EVENT_AND_SCHEDULE_DRIVEN"
        );
    }
}
