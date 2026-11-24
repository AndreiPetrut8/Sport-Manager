package com.teammanager.core.domain;

import com.teammanager.contracts.domain.PlayerStatus;
import com.teammanager.core.domain.converter.JsonMapAttributeConverter;
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
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    /**
     * Sensitive PII: kept inside core-service & UI; stripped by mcp-server before reaching any external LLM.
     */
    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false)
    private int jerseyNumber;

    @Column(nullable = false, length = 48)
    private String position;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private PlayerStatus status;

    /**
     * Polymorphic sport-specific metrics (e.g. pace/stamina for football, wingspan/threePointPct for basketball).
     * Stored as an unmodifiable defensive snapshot in memory for thread-safe reads.
     */
    @Convert(converter = JsonMapAttributeConverter.class)
    @Column(name = "attributes_json", nullable = false, columnDefinition = "TEXT")
    private Map<String, Object> attributes = Collections.emptyMap();

    @Version
    private Long version;

    protected Player() {
    }

    public Player(Team team, String fullName, int jerseyNumber, String position,
                  PlayerStatus status, Map<String, Object> attributes) {
        this.team = team;
        this.fullName = fullName;
        this.jerseyNumber = jerseyNumber;
        this.position = position;
        this.status = status;
        setAttributes(attributes);
    }

    public Long getId() {
        return id;
    }

    public Team getTeam() {
        return team;
    }

    public String getFullName() {
        return fullName;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public PlayerStatus getStatus() {
        return status;
    }

    public void setStatus(PlayerStatus status) {
        this.status = status;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public final void setAttributes(Map<String, Object> attributes) {
        this.attributes = (attributes == null || attributes.isEmpty())
                ? Collections.emptyMap()
                : Collections.unmodifiableMap(new LinkedHashMap<>(attributes));
    }

    public Long getVersion() {
        return version;
    }
}
