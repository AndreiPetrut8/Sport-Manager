package com.teammanager.core.repository;

import com.teammanager.contracts.domain.SportCode;
import com.teammanager.core.domain.Sport;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportRepository extends JpaRepository<Sport, Long> {
    Optional<Sport> findByCode(SportCode code);
}
