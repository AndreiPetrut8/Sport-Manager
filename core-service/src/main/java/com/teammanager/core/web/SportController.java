package com.teammanager.core.web;

import com.teammanager.contracts.domain.SportCode;
import com.teammanager.core.repository.SportRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sports")
@Tag(name = "Sports Catalog", description = "Supported sports and field/roster rules")
public class SportController {

    private final SportRepository sportRepository;

    public SportController(SportRepository sportRepository) {
        this.sportRepository = sportRepository;
    }

    @GetMapping
    @Operation(summary = "List all supported sports from the Single-Source-of-Truth DB")
    public List<SportDto> listSports() {
        return sportRepository.findAll().stream()
                .map(s -> new SportDto(s.getId(), s.getCode(), s.getName(), s.getPlayersOnField(), s.getMaxRosterSize()))
                .toList();
    }

    public record SportDto(Long id, SportCode code, String name, int playersOnField, int maxRosterSize) {
    }
}
