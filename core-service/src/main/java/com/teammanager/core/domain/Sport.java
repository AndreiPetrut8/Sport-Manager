package com.teammanager.core.domain;

import com.teammanager.contracts.domain.SportCode;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "sports")
public class Sport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 32)
    private SportCode code;

    @Column(nullable = false, length = 80)
    private String name;

    @Column(nullable = false)
    private int playersOnField;

    @Column(nullable = false)
    private int maxRosterSize;

    protected Sport() {
    }

    public Sport(SportCode code, String name, int playersOnField, int maxRosterSize) {
        this.code = code;
        this.name = name;
        this.playersOnField = playersOnField;
        this.maxRosterSize = maxRosterSize;
    }

    public Long getId() {
        return id;
    }

    public SportCode getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getPlayersOnField() {
        return playersOnField;
    }

    public int getMaxRosterSize() {
        return maxRosterSize;
    }
}
