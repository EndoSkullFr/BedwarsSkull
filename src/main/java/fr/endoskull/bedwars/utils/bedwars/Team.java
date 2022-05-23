package fr.endoskull.bedwars.utils.bedwars;

import fr.endoskull.bedwars.utils.Upgrades;

public class Team {

    private String name;
    private TeamColor color;
    private boolean hasBed = true;
    private boolean available = false;
    private TeamUpgrade upgrades;

    public Team(String name, TeamColor color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TeamColor getColor() {
        return color;
    }

    public void setColor(TeamColor color) {
        this.color = color;
    }

    public boolean isHasBed() {
        return hasBed;
    }

    public void setHasBed(boolean hasBed) {
        this.hasBed = hasBed;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setUpgrades(TeamUpgrade upgrades) {
        this.upgrades = upgrades;
    }

    public TeamUpgrade getUpgrades() {
        return upgrades;
    }
}
