package com.teammanager.core.repository;

import com.teammanager.contracts.domain.PlayerStatus;
import com.teammanager.core.domain.Player;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findByTeamId(Long teamId);
    List<Player> findByTeamIdAndStatus(Long teamId, PlayerStatus status);
}
