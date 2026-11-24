package com.teammanager.core.repository;

import com.teammanager.core.domain.Team;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @EntityGraph(attributePaths = "sport")
    List<Team> findByManagerUsername(String managerUsername);
}
