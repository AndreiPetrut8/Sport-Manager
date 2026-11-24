package com.teammanager.core.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;

/**
 * Represents a scheduled fixture (corresponds to MatchManager context in the domain diagram).
 */
@Entity
@Table(name = "matches")
public class MatchFixture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @Column(nullable = false, length = 120)
    private String opponentName;

    @Column(nullable = false)
    private Instant scheduledAt;

    @Column(nullable = false)
    private boolean homeGame;

    @Version
    private Long version;

    protected MatchFixture() {
    }

    public MatchFixture(Team team, String opponentName, Instant scheduledAt, boolean homeGame) {
        this.team = team;
        this.opponentName = opponentName;
        this.scheduledAt = scheduledAt;
        this.homeGame = homeGame;
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public Instant getScheduledAt() {
        return scheduledAt;
    }

    public boolean isHomeGame() {
        return homeGame;
    }

    public Long getVersion() {
        return version;
    }
}
