package com.teammanager.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.teammanager.contracts.domain.PlayerStatus;
import com.teammanager.contracts.domain.ProposalStatus;
import com.teammanager.contracts.domain.SportCode;
import com.teammanager.core.domain.MatchFixture;
import com.teammanager.core.domain.Player;
import com.teammanager.core.domain.Sport;
import com.teammanager.core.domain.TacticalProposal;
import com.teammanager.core.domain.Team;
import com.teammanager.core.repository.MatchFixtureRepository;
import com.teammanager.core.repository.PlayerRepository;
import com.teammanager.core.repository.SportRepository;
import com.teammanager.core.repository.TacticalProposalRepository;
import com.teammanager.core.repository.TeamRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "SQLITE_URL=jdbc:sqlite:./target/core-integration-test.db?journal_mode=WAL&busy_timeout=5000&foreign_keys=on",
        "app.messaging.broker-enabled=false"
})
class CoreDatabaseIntegrationTest {

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private MatchFixtureRepository matchFixtureRepository;

    @Autowired
    private TacticalProposalRepository tacticalProposalRepository;

    @Test
    void persistsMultiSportPolymorphicPlayersAndPendingProposals() {
        Sport football = sportRepository.findByCode(SportCode.FOOTBALL).orElseThrow();
        Sport basketball = sportRepository.findByCode(SportCode.BASKETBALL).orElseThrow();

        Team footballTeam = teamRepository.save(new Team("FC Universitatea", football, "coach.alex"));
        Team basketTeam = teamRepository.save(new Team("BC Steaua Nord", basketball, "coach.alex"));

        Player striker = playerRepository.save(new Player(
                footballTeam,
                "Andrei Popescu",
                9,
                "ST",
                PlayerStatus.AVAILABLE,
                Map.of("pace", 88, "finishing", 85, "stamina", 79)
        ));

        Player pointGuard = playerRepository.save(new Player(
                basketTeam,
                "Matei Ionescu",
                4,
                "PG",
                PlayerStatus.AVAILABLE,
                Map.of("wingspanCm", 196, "threePointPct", 41.5, "assistsPerGame", 7.2)
        ));

        Player reloadedStriker = playerRepository.findById(striker.getId()).orElseThrow();
        Player reloadedPg = playerRepository.findById(pointGuard.getId()).orElseThrow();

        assertThat(reloadedStriker.getAttributes()).containsEntry("pace", 88).containsEntry("finishing", 85);
        assertThat(reloadedPg.getAttributes()).containsEntry("wingspanCm", 196);
        assertThatThrownBy(() -> reloadedStriker.getAttributes().put("hack", 99))
                .isInstanceOf(UnsupportedOperationException.class);

        MatchFixture match = matchFixtureRepository.save(
                new MatchFixture(footballTeam, "CS Rapid", Instant.now().plus(3, ChronoUnit.DAYS), true)
        );
        TacticalProposal proposal = tacticalProposalRepository.save(
                new TacticalProposal(match, "4-3-3", List.of(striker.getId()), "High press against slow backline", "tactical-agent")
        );

        assertThat(proposal.getStatus()).isEqualTo(ProposalStatus.PENDING);
        assertThat(tacticalProposalRepository.findByMatchIdAndStatus(match.getId(), ProposalStatus.PENDING))
                .extracting(TacticalProposal::getFormation)
                .contains("4-3-3");
    }

    @Test
    void optimisticLockingPreventsLostUpdatesOnConcurrentEdits() {
        Sport volleyball = sportRepository.findByCode(SportCode.VOLLEYBALL).orElseThrow();
        Team created = teamRepository.save(new Team("Volei Club", volleyball, "coach.dana"));

        // Simulate two concurrent coaching sessions loading the exact same version snapshot
        Team sessionOneCopy = teamRepository.findById(created.getId()).orElseThrow();
        Team sessionTwoCopy = teamRepository.findById(created.getId()).orElseThrow();

        sessionOneCopy.setName("Volei Club Seniori");
        teamRepository.saveAndFlush(sessionOneCopy);

        sessionTwoCopy.setName("Volei Club Stale");
        assertThatThrownBy(() -> teamRepository.saveAndFlush(sessionTwoCopy))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }
}
