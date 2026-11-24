package com.teammanager.core.config;

import com.teammanager.contracts.domain.SportCode;
import com.teammanager.core.domain.Sport;
import com.teammanager.core.repository.SportRepository;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SportCatalogBootstrap {

    private static final Logger log = LoggerFactory.getLogger(SportCatalogBootstrap.class);

    @Bean
    public CommandLineRunner seedDefaultSports(SportRepository sportRepository) {
        return args -> {
            Map<SportCode, String> defaults = Map.of(
                    SportCode.FOOTBALL, "Fotbal",
                    SportCode.BASKETBALL, "Baschet",
                    SportCode.VOLLEYBALL, "Volei"
            );
            defaults.forEach((code, label) -> {
                if (sportRepository.findByCode(code).isEmpty()) {
                    sportRepository.save(new Sport(
                            code,
                            label,
                            code.defaultPlayersOnField(),
                            code.defaultMaxRosterSize()
                    ));
                    log.info("Seeded default sport definition: {} ({})", label, code);
                }
            });
        };
    }
}
