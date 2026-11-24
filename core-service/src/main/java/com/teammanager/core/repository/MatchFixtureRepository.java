package com.teammanager.core.repository;

import com.teammanager.core.domain.MatchFixture;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchFixtureRepository extends JpaRepository<MatchFixture, Long> {
    List<MatchFixture> findByTeamIdOrderByScheduledAtAsc(Long teamId);
}
