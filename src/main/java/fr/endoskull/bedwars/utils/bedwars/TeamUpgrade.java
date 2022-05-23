package fr.endoskull.bedwars.utils.bedwars;

import fr.endoskull.bedwars.utils.Traps;
import fr.endoskull.bedwars.utils.Upgrades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TeamUpgrade {

    private Team team;
    private final HashMap<Upgrades, Integer> upgrades = new HashMap<>();
    private List<Traps> traps = new ArrayList<>();
    private List<Traps> waitingRemove = new ArrayList<>();


    public TeamUpgrade(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public HashMap<Upgrades, Integer> getMap() {
        return upgrades;
    }

    public List<Traps> getTraps() {
        return traps;
    }

    public List<Traps> getWaitingRemove() {
        return waitingRemove;
    }
}
