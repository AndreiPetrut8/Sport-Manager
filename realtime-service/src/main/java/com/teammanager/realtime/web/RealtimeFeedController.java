package com.teammanager.realtime.web;

import com.teammanager.realtime.state.TeamRealtimeProjectionStore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/realtime")
@Tag(name = "Realtime Feed", description = "Event-projected alerts and WebSocket server diagnostics")
public class RealtimeFeedController {

    private final TeamRealtimeProjectionStore projectionStore;

    public RealtimeFeedController(TeamRealtimeProjectionStore projectionStore) {
        this.projectionStore = projectionStore;
    }

    @GetMapping("/teams/{teamId}/alerts")
    @Operation(summary = "Read recent event-driven team alerts from the local thread-safe projection")
    public List<TeamRealtimeProjectionStore.TeamAlertSnapshot> teamAlerts(@PathVariable Long teamId) {
        return projectionStore.recentAlerts(teamId);
    }
}
