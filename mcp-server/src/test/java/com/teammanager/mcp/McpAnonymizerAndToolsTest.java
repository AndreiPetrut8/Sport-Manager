package com.teammanager.mcp;

import static org.assertj.core.api.Assertions.assertThat;

import com.teammanager.mcp.anonymizer.PlayerAnonymizationVault;
import com.teammanager.mcp.tools.McpToolCatalog;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class McpAnonymizerAndToolsTest {

    @Autowired
    private PlayerAnonymizationVault vault;

    @Autowired
    private McpToolCatalog toolCatalog;

    @Test
    void concurrentAliasResolutionProducesSingleConsistentTokenPerPlayer() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(8);
        Set<String> aliasesForSamePlayer = ConcurrentHashMap.newKeySet();
        try {
            List<Callable<Void>> tasks = new ArrayList<>();
            for (int i = 0; i < 40; i++) {
                tasks.add(() -> {
                    aliasesForSamePlayer.add(vault.aliasForPlayer(42L));
                    return null;
                });
            }
            for (Future<Void> f : pool.invokeAll(tasks)) {
                f.get();
            }
            assertThat(aliasesForSamePlayer).hasSize(1);
            String alias = aliasesForSamePlayer.iterator().next();
            assertThat(vault.resolveRealPlayerId(alias)).contains(42L);
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void exposesAtLeastThreeToolsIncludingReadAndProposeChange() {
        assertThat(toolCatalog.registeredTools()).hasSizeGreaterThanOrEqualTo(3);
        assertThat(toolCatalog.registeredTools())
                .extracting(McpToolCatalog.ToolDescriptor::category)
                .contains("READ", "PROPOSE_CHANGE");
    }
}
