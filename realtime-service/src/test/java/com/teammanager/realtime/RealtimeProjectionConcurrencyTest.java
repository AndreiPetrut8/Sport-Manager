package com.teammanager.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import com.teammanager.realtime.state.TeamRealtimeProjectionStore;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RealtimeProjectionConcurrencyTest {

    @Autowired
    private TeamRealtimeProjectionStore projectionStore;

    @Test
    void handlesConcurrentEventWritesAndBoundedSnapshotsSafely() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        try {
            List<Callable<Void>> tasks = new ArrayList<>();
            for (int i = 0; i < 80; i++) {
                int idx = i;
                tasks.add(() -> {
                    projectionStore.appendAlert(1L, "ROSTER_UPDATED", "Event #" + idx, Instant.now());
                    projectionStore.recentAlerts(1L);
                    return null;
                });
            }
            for (Future<Void> f : pool.invokeAll(tasks)) {
                f.get();
            }
            assertThat(projectionStore.recentAlerts(1L)).hasSizeLessThanOrEqualTo(50).isNotEmpty();
        } finally {
            pool.shutdownNow();
        }
    }
}
