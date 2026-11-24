package com.teammanager.core.domain;

import com.teammanager.contracts.domain.ProposalStatus;
import com.teammanager.core.domain.converter.LongListAttributeConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;
import java.util.List;

/**
 * Stores tactical plans proposed either by the autonomous AI agent (via MCP propose_tactical_plan tool)
 * or drafted by the manager. Proposals start as PENDING until explicitly approved/rejected.
 */
@Entity
@Table(name = "tactical_proposals")
public class TacticalProposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "match_id", nullable = false)
    private MatchFixture match;

    @Column(nullable = false, length = 32)
    private String formation;

    @Convert(converter = LongListAttributeConverter.class)
    @Column(name = "starter_player_ids_json", nullable = false, columnDefinition = "TEXT")
    private List<Long> starterPlayerIds = List.of();

    @Column(nullable = false, columnDefinition = "TEXT")
    private String rationale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private ProposalStatus status;

    @Column(nullable = false, length = 64)
    private String proposedBy;

    @Column(nullable = false)
    private Instant createdAt;

    @Version
    private Long version;

    protected TacticalProposal() {
    }

    public TacticalProposal(MatchFixture match, String formation, List<Long> starterPlayerIds,
                            String rationale, String proposedBy) {
        this.match = match;
        this.formation = formation;
        this.starterPlayerIds = starterPlayerIds == null ? List.of() : List.copyOf(starterPlayerIds);
        this.rationale = rationale;
        this.status = ProposalStatus.PENDING;
        this.proposedBy = proposedBy;
        this.createdAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public MatchFixture getMatch() {
        return match;
    }

    public String getFormation() {
        return formation;
    }

    public List<Long> getStarterPlayerIds() {
        return starterPlayerIds;
    }

    public String getRationale() {
        return rationale;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    public void approve() {
        this.status = ProposalStatus.APPROVED;
    }

    public void reject() {
        this.status = ProposalStatus.REJECTED;
    }

    public String getProposedBy() {
        return proposedBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Long getVersion() {
        return version;
    }
}
