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

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sport_id", nullable = false)
    private Sport sport;

    @Column(nullable = false, length = 80)
    private String managerUsername;

    /**
     * Optimistic lock guarding against lost updates when two coaching staff sessions edit team metadata concurrently.
     */
    @Version
    private Long version;

    protected Team() {
    }

    public Team(String name, Sport sport, String managerUsername) {
        this.name = name;
        this.sport = sport;
        this.managerUsername = managerUsername;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sport getSport() {
        return sport;
    }

    public String getManagerUsername() {
        return managerUsername;
    }

    public Long getVersion() {
        return version;
    }
}
