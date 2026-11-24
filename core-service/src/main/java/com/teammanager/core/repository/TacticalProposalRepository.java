package com.teammanager.core.repository;

import com.teammanager.contracts.domain.ProposalStatus;
import com.teammanager.core.domain.TacticalProposal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TacticalProposalRepository extends JpaRepository<TacticalProposal, Long> {
    List<TacticalProposal> findByMatchIdAndStatus(Long matchId, ProposalStatus status);
}
