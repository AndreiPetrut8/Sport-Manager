package com.teammanager.contracts.domain;

public enum SportCode {
    FOOTBALL(11, 25),
    BASKETBALL(5, 15),
    VOLLEYBALL(6, 14);

    private final int defaultPlayersOnField;
    private final int defaultMaxRosterSize;

    SportCode(int defaultPlayersOnField, int defaultMaxRosterSize) {
        this.defaultPlayersOnField = defaultPlayersOnField;
        this.defaultMaxRosterSize = defaultMaxRosterSize;
    }

    public int defaultPlayersOnField() {
        return defaultPlayersOnField;
    }

    public int defaultMaxRosterSize() {
        return defaultMaxRosterSize;
    }
}
